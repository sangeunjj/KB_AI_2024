package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.entity.Member;
import com.kbai.corporatefinance.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member login(String username, String password) {
        return memberRepository.findByUsernameAndPassword(username, password);
    }
}

