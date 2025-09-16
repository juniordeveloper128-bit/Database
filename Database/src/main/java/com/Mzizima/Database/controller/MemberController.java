package com.Mzizima.Database.controller;

import com.Mzizima.Database.entity.Member;
import com.Mzizima.Database.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        if (memberService.authenticate(email, password)) {
            Optional<Member> member = memberService.findByEmail(email);
            if (member.isPresent()) {
                session.setAttribute("loggedInMember", member.get());
                return "redirect:/dashboard";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Invalid email or password");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Member member, BindingResult result,
                           @RequestParam("passportPhoto") MultipartFile file,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        // Check if email or membership number already exists
        if (memberService.findByEmail(member.getEmail()).isPresent()) {
            result.rejectValue("email", "error.member", "Email already exists");
            return "register";
        }

        if (memberService.findByMembershipNumber(member.getMembershipNumber()).isPresent()) {
            result.rejectValue("membershipNumber", "error.member", "Membership number already exists");
            return "register";
        }

        try {
            // Save passport photo
            if (!file.isEmpty()) {
                String photoPath = memberService.savePassportPhoto(file);
                member.setPassportPhotoPath(photoPath);
            }

            memberService.saveMember(member);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error uploading photo: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return "redirect:/login";
        }

        // Regular members should not access dashboard - redirect to profile
        if (loggedInMember.getRole() == Member.Role.MEMBER) {
            redirectAttributes.addFlashAttribute("info", "Access restricted. You can view your profile here.");
            return "redirect:/profile";
        }

        model.addAttribute("member", loggedInMember);

        // Get membership statistics
        Map<String, Object> stats = memberService.getMembershipStatistics();
        model.addAttribute("stats", stats);

        // Show all members for admin users
        List<Member> allMembers = memberService.getAllMembers();
        model.addAttribute("allMembers", allMembers);

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("member", loggedInMember);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}