package mk.ukim.finki.soa.graduationthesis.infrastructure.messaging

import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.springframework.stereotype.Component

@Component
class EventPublisher(private val eventBus: EventBus) {

    fun registerDispatchInterceptor(interceptor: MessageDispatchInterceptor<in EventMessage<*>>) {
        eventBus.registerDispatchInterceptor(interceptor)
    }

    fun publish(event: Any) {
        eventBus.publish(GenericEventMessage.asEventMessage<Any>(event))
    }
}