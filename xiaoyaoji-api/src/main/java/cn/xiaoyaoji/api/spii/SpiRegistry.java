package cn.xiaoyaoji.api.spii;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * <p>
 * 注册第三方插件
 *
 * @author: zhoujingjie
 * Date: 2018/9/21
 */
public class SpiRegistry implements ImportBeanDefinitionRegistrar {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
        Environment environment = (StandardEnvironment) beanFactory.getBean("environment");
        registerPlugins(environment, beanFactory, "xyj.provider.cache", "xyj.provider.upload", "xyj.provider.email");
    }

    private void registerPlugins(Environment environment, DefaultListableBeanFactory factory, String... pluginPropertyName) {
        for (String propertyName : pluginPropertyName) {
            String cacheBeanName = environment.getProperty(propertyName);
            if (Strings.isNullOrEmpty(cacheBeanName)) {
                logger.error("not found property {}", propertyName);
                return;
            }
            register(cacheBeanName, factory);
        }
    }


    private void register(String beanName, DefaultListableBeanFactory registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanName);
        registry.registerBeanDefinition(registry.canonicalName(beanName), builder.getBeanDefinition());
    }
}
