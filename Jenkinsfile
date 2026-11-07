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


    }
}
