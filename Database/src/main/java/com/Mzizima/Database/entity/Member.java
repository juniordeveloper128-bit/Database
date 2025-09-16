package com.Mzizima.Database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @NotBlank(message = "Membership number is required")
    @Column(name = "membership_number", nullable = false, unique = true)
    private String membershipNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @NotBlank(message = "Living place is required")
    @Column(name = "living_place", nullable = false)
    private String livingPlace;

    @Column(name = "living_street")
    private String livingStreet;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Column(name = "passport_photo_path")
    private String passportPhotoPath;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // Enums
    public enum MemberType {
        LOCAL_MEMBER, GUEST
    }

    public enum Gender {
        MALE, FEMALE
    }

    public enum MaritalStatus {
        SINGLE, MARRIED, DIVORCED, WIDOWED
    }

    public enum Role {
        MEMBER, ELDER, CHURCH_SECRETARY, SHEPHERD
    }

    // Constructors
    public Member() {
        this.createdAt = LocalDate.now();
        this.joinDate = LocalDate.now();
    }

    public Member(String fullName, String email, String contactNumber, String membershipNumber,
                  MemberType memberType, String livingPlace, Gender gender, Role role) {
        this();
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.membershipNumber = membershipNumber;
        this.memberType = memberType;
        this.livingPlace = livingPlace;
        this.gender = gender;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }

    public String getMembershipNumber() { return membershipNumber; }
    public void setMembershipNumber(String membershipNumber) { this.membershipNumber = membershipNumber; }

    public MemberType getMemberType() { return memberType; }
    public void setMemberType(MemberType memberType) { this.memberType = memberType; }

    public String getLivingPlace() { return livingPlace; }
    public void setLivingPlace(String livingPlace) { this.livingPlace = livingPlace; }

    public String getLivingStreet() { return livingStreet; }
    public void setLivingStreet(String livingStreet) { this.livingStreet = livingStreet; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public MaritalStatus getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(MaritalStatus maritalStatus) { this.maritalStatus = maritalStatus; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public String getPassportPhotoPath() { return passportPhotoPath; }
    public void setPassportPhotoPath(String passportPhotoPath) { this.passportPhotoPath = passportPhotoPath; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    // Helper method to check if user has administrative access
    public boolean hasAdminAccess() {
        return role == Role.ELDER || role == Role.CHURCH_SECRETARY || role == Role.SHEPHERD;
    }
}