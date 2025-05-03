package mk.ukim.finki.soa.graduationthesis.application.config

import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.scheduling.quartz.EventJobDataBinder
import org.axonframework.eventhandling.scheduling.quartz.QuartzEventScheduler
import org.quartz.Scheduler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {
    @Bean
    fun eventScheduler(
        eventBus: EventBus,
        scheduler: Scheduler,
        eventJobDataBinder: EventJobDataBinder,
        transactionManager: TransactionManager
    ): QuartzEventScheduler? {

        return QuartzEventScheduler.builder()
            .eventBus(eventBus)
            .transactionManager(transactionManager)
            .scheduler(scheduler)
            .jobDataBinder(eventJobDataBinder)
            .build()
    }
}
