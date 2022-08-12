package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DBJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // Note : Job을 생성하는 빌드 펙토리
    private final StepBuilderFactory stepBuilderFactory; // Note : step을 생성하는 빌드 펙토리

    // Note : 배치 기본 흐름
    //   Job 구동 -> Step 실행 -> Tasklet(step안에서 이루어 지는 단일 테스크) 실행

    @Bean
    public Job helloJob() {
        return this.jobBuilderFactory.get("Job") // Note : 'Job' 이름으로 Job 생성
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1") // Note : 'step1' 이름으로 Job 생성
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 has executed");
                        // Note : tasklet에서 반환값으로 얼마나 반복시킬지 설정할 수 있음
                        return RepeatStatus.FINISHED; // Note : 한번만 수행하고 tasklet 종료시킴
                    }
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                // Note : step1()의 Tasklet을 lamda식으로 바꾼것임
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}