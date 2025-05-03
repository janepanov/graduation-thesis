package mk.ukim.finki.soa.graduationthesis.infrastructure.persistence

import mk.ukim.finki.soa.graduationthesis.domain.valueobject.ThesisStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ThesisViewRepository : JpaRepository<ThesisView, String> {
    fun findByStudentId(studentId: String): List<ThesisView>
    fun findByMentorId(mentorId: String): List<ThesisView>
    fun findByStatus(status: ThesisStatus): List<ThesisView>
    fun findByMentorIdAndStatus(mentorId: String, status: ThesisStatus): List<ThesisView>
    fun findByStudentIdAndStatus(studentId: String, status: ThesisStatus): List<ThesisView>
}