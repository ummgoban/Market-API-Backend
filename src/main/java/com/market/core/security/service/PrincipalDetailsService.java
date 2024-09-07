package com.market.core.security.service;

import com.market.core.code.MemberErrorCode;
import com.market.core.error.exception.security.MemberOAuthIdNotFoundException;
import com.market.core.security.model.PrincipalDetails;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String oauthId) throws UsernameNotFoundException {
        Member member = memberRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new MemberOAuthIdNotFoundException(MemberErrorCode.NOT_FOUND_MEMBER_OAUTH_ID));

        return new PrincipalDetails(member);
    }
}
