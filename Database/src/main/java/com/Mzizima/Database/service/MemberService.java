package com.Mzizima.Database.service;

import com.Mzizima.Database.entity.Member;
import com.Mzizima.Database.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String UPLOAD_DIR = "uploads/passport-photos/";

    public Member saveMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByMembershipNumber(String membershipNumber) {
        return memberRepository.findByMembershipNumber(membershipNumber);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public String savePassportPhoto(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Save file
        Files.copy(file.getInputStream(), filePath);

        return UPLOAD_DIR + fileName;
    }

    public Map<String, Object> getMembershipStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalMembers", memberRepository.countTotalMembers());
        stats.put("localMembers", memberRepository.countByMemberType(Member.MemberType.LOCAL_MEMBER));
        stats.put("guests", memberRepository.countByMemberType(Member.MemberType.GUEST));
        stats.put("elders", memberRepository.countByRole(Member.Role.ELDER));
        stats.put("churchSecretaries", memberRepository.countByRole(Member.Role.CHURCH_SECRETARY));
        stats.put("shepherds", memberRepository.countByRole(Member.Role.SHEPHERD));
        stats.put("regularMembers", memberRepository.countByRole(Member.Role.MEMBER));

        return stats;
    }

    public boolean authenticate(String email, String password) {
        Optional<Member> member = findByEmail(email);
        if (member.isPresent()) {
            return passwordEncoder.matches(password, member.get().getPassword());
        }
        return false;
    }
}