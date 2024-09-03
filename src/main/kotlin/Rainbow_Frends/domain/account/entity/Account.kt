package Rainbow_Frends.domain.account.entity

import jakarta.persistence.*

@Entity
class Account {
    @Id
    @Column(name = "studentId", unique = true, nullable = false)
    var studentId: Int? = null

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role? = null

    @Column(name = "profilePictureURL")
    var profilePictureURL: String? = null

    @Column(name = "profilePictureName")
    var profilePictureName: String? = null

    @Column(name = "lateNumber", nullable = false)
    var lateNumber: Int? = null

    @Column(name = "floor")
    var floor: Int? = null

    @Column(name = "roomNumber")
    var roomNumber: Int? = null
}