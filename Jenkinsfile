
node{
  stage('Get Git code'){
  git 'https://github.com/HitaJoshi/waterOrders.git'
  }
  stage('compile and package'){
  def mvnHome = tool name: '', type: 'maven'
    sh "${mvnHome}/bin/mvn package"
  }
  }
