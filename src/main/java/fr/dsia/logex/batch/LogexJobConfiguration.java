package fr.dsia.logex.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import fr.dsia.logex.batch.processor.RequestItemProcessor;
import fr.dsia.logex.model.CsvLineDto;
import fr.dsia.logex.model.LogLineDto;

@Configuration
@EnableBatchProcessing
public class LogexJobConfiguration {

	@Value("input/NLW_20200124.log")
	private Resource inputFile;

	private Resource outputFile = new FileSystemResource("output/outputData.csv");

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job logexJob(Step step1) {
		return jobBuilderFactory.get("logexJob").flow(step1).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<LogLineDto, CsvLineDto>chunk(10) //
				.reader(logFileItemReader()) //
				.processor(requestProcessor()) //
				.writer(csvFileWriter()) //
				.build();
	}

	@Bean
	FlatFileItemReader<LogLineDto> logFileItemReader() {
		FlatFileItemReader<LogLineDto> logfileReader = new FlatFileItemReader<>();
		logfileReader.setResource(inputFile);
		logfileReader.setLinesToSkip(1);

		LineMapper<LogLineDto> studentLineMapper = createRequestLineMapper();
		logfileReader.setLineMapper(studentLineMapper);

		return logfileReader;
	}

	private LineMapper<LogLineDto> createRequestLineMapper() {
		DefaultLineMapper<LogLineDto> requestLineMapper = new DefaultLineMapper<>();

		LineTokenizer requestLineTokenizer = createRequestLineTokenizer();
		requestLineMapper.setLineTokenizer(requestLineTokenizer);

		FieldSetMapper<LogLineDto> studentInformationMapper = createStudentInformationMapper();
		requestLineMapper.setFieldSetMapper(studentInformationMapper);

		return requestLineMapper;
	}

	private LineTokenizer createRequestLineTokenizer() {
		DelimitedLineTokenizer studentLineTokenizer = new DelimitedLineTokenizer();
		studentLineTokenizer.setDelimiter(" - ");
		studentLineTokenizer.setNames(new String[] { "date", "headers" });
		return studentLineTokenizer;
	}

	private FieldSetMapper<LogLineDto> createStudentInformationMapper() {
		BeanWrapperFieldSetMapper<LogLineDto> requestInformationMapper = new BeanWrapperFieldSetMapper<>();
		requestInformationMapper.setTargetType(LogLineDto.class);
		return requestInformationMapper;
	}

	@Bean
	public RequestItemProcessor requestProcessor() {
		return new RequestItemProcessor();
	}

	@Bean
	public FlatFileItemWriter<CsvLineDto> csvFileWriter() {
		FlatFileItemWriter<CsvLineDto> writer = new FlatFileItemWriter<>();
		writer.setResource(outputFile);
		writer.setAppendAllowed(false);
		writer.setLineAggregator(new DelimitedLineAggregator<CsvLineDto>() {
			{
				setDelimiter(";");
				setFieldExtractor(new BeanWrapperFieldExtractor<CsvLineDto>() {
					{
						setNames(new String[] { "requestDateTime", "responseDateTime", "duration", "status", "params", "uri" });
					}
				});
			}
		});
		return writer;
	}
}
