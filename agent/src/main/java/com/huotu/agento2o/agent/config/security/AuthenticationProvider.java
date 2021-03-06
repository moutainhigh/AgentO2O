/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 *
 */

package com.huotu.agento2o.agent.config.security;

import com.huotu.agento2o.service.common.RoleTypeEnum;
import com.huotu.agento2o.service.config.MallPasswordEncoder;
import com.huotu.agento2o.service.entity.MallCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * Created by helloztt on 2016/5/9.
 */
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationManager {
    @Autowired
    private MallPasswordEncoder passwordEncoder;
    @Resource(name = "mallCustomerService")
    private UserDetailsService mallCustomerService;

    /**
     * 校验密码存在
     *
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //在filter中已经校验密码为空的情况，此处可省略
        /*if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }*/
        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    /**
     * 校验用户名存在
     *
     * @param username
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        UserDetails loadedUser;
        try {
            loadedUser = this.getCurrentService(authentication).loadUserByUsername(username);
            if(loadedUser != null && loadedUser instanceof MallCustomer){
                if(((MallCustomer) loadedUser).getCustomerType().getCode() != authenticationToken.getRoleType()){
                    throw new UsernameNotFoundException("用户名或密码错误");
                }
            }
        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    protected UserDetailsService getCurrentService(UsernamePasswordAuthenticationToken authentication) {
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        UserDetailsService currentService = null;

        if (authenticationToken.getRoleType() == RoleTypeEnum.AGENT.getCode() || authenticationToken.getRoleType() == RoleTypeEnum.SHOP.getCode()) {
            currentService = mallCustomerService;
        }
        return currentService;
    }
}
