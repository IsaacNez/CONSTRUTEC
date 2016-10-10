angular.module('employeeView',[])
.controller('projectCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
    // get ingenieers and architects from DB
    var users;
    var Project;
    var Products = "";
    var Quantities = "" ;
    var productList;
    var stageList;
    // get ingenieers and architects from DB
    $http.get($scope.url+'/api/user/undefined')
            .then( function (response) {    
              $scope.users = response.data;           
        });
      // get productsfrom DB
    $http.get($scope.url+'/api/product/undefined')
            .then( function (response) {    
              $scope.productList = response.data;           
        });
      
    
       
    $scope.createProject() = function () {
         var Project = {
            "P_ID": $scope.P_ID,
            "P_Name": $scope.P_Name,
            "P_Location": $scope.P_Location,
            "P_Budget": $scope.P_Budget,
            "U_Code": $scope.U_Code,
            "S_ID":$scope.stageList,
            "Products":$scope.Products,
            "Quantitys":$scope.Quantities
        }
        console.log(Project);
        
      
        
        $http.post($scope.url+'/api/project/post',Project).
        success(function (data, status, headers, config) {
            alert('the new user has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new employee')
        });
        
        
    }
     $scope.addToStage(pStage) = function (value1,value2){
          
          if(products=="" && amounts=="" ){
              products=value1;
              amounts=value2;
              
          }
          else{
              products = products+ "," + value1 ;
              amounts = amounts+","  + value2  ;
          }
          console.log(products+'/'+amounts);
           
      }
     $scope.addStage(pStage) = function (value1,value2){
         
     }
     
    
}