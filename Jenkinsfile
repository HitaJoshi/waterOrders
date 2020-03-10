
node{
  stage('Get Git code'){
  git 'https://github.com/HitaJoshi/waterOrders/new/master/Servers/water_api'
  }
  stage('compile and package'){
  sh mvn package
  }
  }
