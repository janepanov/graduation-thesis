package mk.ukim.finki.soa.graduationthesis.infrastructure.persistence

import jakarta.persistence.*
import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import java.time.Instant

@Entity
@Table(name = "thesis_view")
data class ThesisView(
    @Id
    val thesisId: String,

    var title: String,
    var area: String,
    var description: String,

    var studentId: String,
    var mentorId: String,
    var firstMemberId: String,
    var secondMemberId: String,

    @Enumerated(EnumType.STRING)
    var status: ThesisStatus,

    var fileId: String? = null,
    var fileName: String? = null,

    var location: String? = null,
    var presentationDateTime: String? = null,

    var grade: Int? = null,

    var createdAt: Instant,
    var updatedAt: Instant
) {
    // No-args constructor required by JPA
    constructor() : this(
        thesisId = "",
        title = "",
        area = "",
        description = "",
        studentId = "",
        mentorId = "",
        firstMemberId = "",
        secondMemberId = "",
        status = ThesisStatus.DRAFT,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )
}