package lost42.backend.common.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.auth.dto.CustomOAuth2User;
import lost42.backend.common.auth.dto.OauthAttribute;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

//        String registrationId = userRequest.getClientRegistration().getRegistrationId(); -> oauth 시스템을 확장하게 되면 쓸 것
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OauthAttribute attributes = OauthAttribute.of(userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        attributes.updateMemberId(member.getMemberId());

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey(),
                attributes.getMemberId(),
                attributes.getOauthProvider()
        );
    }

    private Member saveOrUpdate(OauthAttribute attribute) {
        Member member = memberRepository.findByEmail(attribute.getEmail())
                .map(entity -> entity.update(attribute.getName()))
                .orElse(attribute.toEntity());

        return memberRepository.save(member);
    }
}
