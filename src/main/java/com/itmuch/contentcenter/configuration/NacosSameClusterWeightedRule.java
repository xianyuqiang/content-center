package com.itmuch.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: content-center
 * @description: 同一集群下优先调用
 * @author: xianyuqiang
 * @create: 2021-09-09 11:01
 **/
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            // 想要请求的微服务名称
            String name = baseLoadBalancer.getName();
            // 拿到服务相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // 1.找到指定服务的所有实例A
            List<Instance> instances = namingService.selectInstances(name, true);

            // 2.过滤相同集群下的所有实例B
            List<Instance> collect = instances.stream().filter(instance ->
                    Objects.equals(instance.getClusterName(), clusterName)
            ).collect(Collectors.toList());

            // 3.如果B是空就用A
            List<Instance> instances1 = new ArrayList<>();
            if (CollectionUtils.isEmpty(collect)) {
                instances1 = instances;
                log.warn("跨集群调用");
            } else {
                instances1 = collect;
            }
            // 4.基于权重的负载均衡算法，返回1个实例
            Instance hostByRandomWeight2 = ExtendBalancer.getHostByRandomWeight2(instances1);
            log.info("选择的实例，port={}", hostByRandomWeight2.getPort());
            return new NacosServer(hostByRandomWeight2);
        } catch (NacosException e) {
            log.error("发送异常了..", e);
            return null;
        }
    }
}

class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
