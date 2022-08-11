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
public class HelloJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // Note : Job을 생성하는 빌드 펙토리
    private final StepBuilderFactory stepBuilderFactory; // Note : step을 생성하는 빌드 펙토리

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob") // Note : helloJob 이름으로 Job 생성
                // Note : Job의 Step 설정
                .start(helloStep1())
                .next(helloStep2())
                .build();

    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1") // Note : helloStep1 이름으로 Job 생성
                // Note : Step안에서 단일 테스크로 수행되는 로직 구현 (tasklet)
                // Note : Step에서는 기본적으로 tasklet을 무한으로 반복시킴
                //        tasklet에서 반환값으로 얼마나 반복시킬지 설정할 수 있음 ex) RepeatStatus.FINISHED
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" ==========================");
                        System.out.println(" >> Hello Spring Batch!!");
                        System.out.println(" ==========================");
                        return RepeatStatus.FINISHED; // Note : 한번만 수행하고 tasklet 종료시킴
                    }
                })
                .build();
    }

    private Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" ==========================");
                        System.out.println(" >> Step 2 was executed!!");
                        System.out.println(" ==========================");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    // Note : Job 구동 -> Step 실행 -> Tasklet 실행
}
