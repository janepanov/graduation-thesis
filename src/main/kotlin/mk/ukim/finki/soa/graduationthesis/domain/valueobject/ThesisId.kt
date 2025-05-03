package mk.ukim.finki.soa.graduationthesis.domain.valueobject

import java.io.Serializable

data class ThesisId(val value: String) : Serializable {
    override fun toString(): String = value
}