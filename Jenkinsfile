
node{
  stage('Get Git code'){
  git 'https://github.com/HitaJoshi/waterOrders.git'
  }
  pipeline {
  agent any
  tools {
    maven 'M3'
  }
  stage('compile and package'){
  //def mvnHome = tool name: 'M3', type: 'maven'
   def mvn_version = 'M3'
//withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
  sh 'mvn clean package'
    //sh "${mvnHome}/bin/mvn package"
  }
  }
}
