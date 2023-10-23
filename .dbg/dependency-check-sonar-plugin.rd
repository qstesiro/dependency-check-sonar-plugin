# 编译
{
    alias copy-plugin="cp sonar-dependency-check-plugin/target/sonar-dependency-check-plugin-2.0.8.1.jar ~/github.com/qstesiro/demo/sonar/sonarqube-plugin/extensions/plugins/ &&
                        cp sonar-dependency-check-plugin/target/sonar-dependency-check-plugin-2.0.8.1.jar /media/sf_share/"
    alias sonar-restart="curl http://localhost:9000/api/system/restart -u 'admin:iwpyuh' -X POST"

    alias clean='mvn clean'

    alias build='mvn package -Dmaven.test.skip=true -Dlicense.skip=true && copy-plugin && sonar-restart'
}

# 扫描
{
    # 漏洞扫描
    mvn org.owasp:dependency-check-maven:7.1.0:aggregate \
        -Dname='sonar.dependency-check-sonar-plugin' \
        -Dformats='JSON,HTML' \
        -DassemblyAnalyzerEnabled=false \
        -DretireJsAnalyzerEnabled=false \
        -DretireJsAnalyzerEnabled=false \
        -DskipSystemScope=true \
        -DautoUpdate=false \
        -pl sonar-dependency-check-plugin -am

    # 报告上报
    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar \
        -Dsonar.host.url=http://localhost:9000 \
        -Dsonar.login=admin \
        -Dsonar.password=iwpyuh \
        -Dsonar.projectKey=sonar.dependency-check-sonar-plugin.v \
        -Dsonar.projectName=sonar.dependency-check-sonar-plugin.v \
        -Dsonar.projectVersion=1 \
        -Dsonar.working.directory=.vscannerwork \
        -Dsonar.exclusions=**/*.* \
        -Dsonar.test.exclusions=**/*.* \
        -Dsonar.dependencyCheck.jsonReportPath=./target/dependency-check-report.json \
        -Dsonar.dependencyCheck.htmlReportPath=./target/dependency-check-report.html \
        -pl sonar-dependency-check-plugin -am \
        -Dmaven.test.skip=true
}
