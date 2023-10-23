# 编译
{
    alias copy-plugin="cp sonar-dependency-check-plugin/target/sonar-dependency-check-plugin-2.0.8.jar ~/github.com/qstesiro/demo/sonar/sonarqube-plugin/extensions/plugins/ &&
                        cp sonar-dependency-check-plugin/target/sonar-dependency-check-plugin-2.0.8.jar /media/sf_share/"
    alias sonar-restart='curl http://localhost:9000/api/system/restart -u 'admin:iwpyuh' -X POST'

    alias clean='mvn clean'

    alias build='mvn package -Dmaven.test.skip=true -Dlicense.skip=true && copy-plugin && sonar-restart'
}
