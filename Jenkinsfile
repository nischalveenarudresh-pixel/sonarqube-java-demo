
pipeline {
    agent {
        label 'label1'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nischalveenarudresh-pixel/sonarqube-java-demo.git'
            }
        }

        stage('Sonar Analysis') {
            steps {
                echo 'SonarQube analysis will be configured here'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

