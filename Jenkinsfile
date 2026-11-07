pipeline {
    agent {
        label 'agent1'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nischalveenarudresh-pixel/sonarqube-java-demo.git'
            }
        }

         stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('SonarQube-Server') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.8.0.7211:sonar \
                          -Dsonar.projectKey=DevOps-Java-App \
                          -Dsonar.projectName=DevOps-Java-App \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }

                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()

                        if (qg.status != 'OK') {
                            error "Pipeline aborted: Quality Gate FAILED!"
                        }

                        echo "Quality Gate PASSED!"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        
stage('Upload WAR to Nexus') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'nexus-creds',
                usernameVariable: 'NEXUS_USERNAME',
                passwordVariable: 'NEXUS_PASSWORD'
            )
        ]) {
            sh '''
                SETTINGS=$(mktemp)

                trap 'rm -f "$SETTINGS"' EXIT

                cat > "$SETTINGS" <<EOF
<settings>
    <servers>
        <server>
            <id>nexus-snapshots</id>
            <username>${NEXUS_USERNAME}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
EOF

                mvn -s "$SETTINGS" deploy -DskipTests
            '''
        }
    }
}


      
stage('Deploy to Tomcat') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'nexus-creds',
                usernameVariable: 'NEXUS_USERNAME',
                passwordVariable: 'NEXUS_PASSWORD'
            ),
            sshUserPrivateKey(
                credentialsId: 'tomcat-ssh',
                keyFileVariable: 'SSH_KEY',
                usernameVariable: 'SSH_USER'
            )
        ]) {
            sh '''
                set +x
                umask 077

                SETTINGS=$(mktemp)
                trap 'rm -f "$SETTINGS"' EXIT

                cat > "$SETTINGS" <<EOF
<settings>
    <servers>
        <server>
            <id>nexus-snapshots</id>
            <username>${NEXUS_USERNAME}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
EOF

                # Download WAR from Nexus
                mvn -s "$SETTINGS" \
                  org.apache.maven.plugins:maven-dependency-plugin:3.8.1:get \
                  -Dartifact=com.example:sonarqube-java-demo:1.0-SNAPSHOT:war \
                  -DremoteRepositories=nexus-snapshots::default::http://172.31.6.141:8081/repository/maven-snapshots/

                # Find downloaded WAR
                WAR=$(find "$HOME/.m2/repository/com/example/sonarqube-java-demo/1.0-SNAPSHOT" \
                      -name '*.war' -type f | head -1)

                test -n "$WAR"

                # Transfer WAR to Tomcat EC2
                scp -i "$SSH_KEY" -o StrictHostKeyChecking=accept-new \
                    "$WAR" "$SSH_USER@172.31.17.76:/tmp/sonarqube-java-demo.war"

                # Stop Tomcat, deploy WAR, restart Tomcat
                ssh -i "$SSH_KEY" -o StrictHostKeyChecking=accept-new \
                    "$SSH_USER@172.31.17.76" <<'REMOTE'

sudo systemctl stop tomcat

sudo rm -rf /opt/tomcat/webapps/sonarqube-java-demo

sudo install -o tomcat -g tomcat -m 0644 \
    /tmp/sonarqube-java-demo.war \
    /opt/tomcat/webapps/sonarqube-java-demo.war

sudo rm -f /tmp/sonarqube-java-demo.war

sudo systemctl start tomcat

sudo systemctl status tomcat --no-pager

REMOTE
            '''
        }
    }
}
  


    }
}
