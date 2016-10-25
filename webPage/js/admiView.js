var actualProject;
var actualStage;
var url = 'http://desktop-6upj287:7575';
var stageForm = angular.module('admiView',[])
.controller('productCtrl', ['$scope', '$http', function ($scope, $http) {
  
    document.getElementById("idUser").innerHTML = "Welcome "+userID;
    // get ingenieers and architects from DB
    var Users;

     // Get the modal
        var modalProducts = document.getElementById('productModal');
      

        // Get the button that opens the modal
        var product = document.getElementById("getProducts");
    

        // Get the <span> element that closes the modal
        var span1 = document.getElementById("close1");
     

        // When the user clicks the button, open the modal
        product.onclick = function() {
            
            // get all products from the EPA-TEC core
           
       $http.get(url+'/api/Admin/get/product/undefined')
            .then( function (response) {
            $scope.productList = response.data;
           console.log($scope.productList);
           modalProducts.style.display = "block";
         });
            
        }

        span1.onclick = function() {
            modalProducts.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modalProducts) {
                modalProducts.style.display = "none";
            }
        }


    
          
  
}]);

stageForm = angular.module('admiView')
.controller('employeeCtrl', ['$scope', '$http', function ($scope, $http) {
    
   // Get the modal
        var modalEmployees = document.getElementById('employeeModal');
        
        
    
        // Get the button that opens the modal
        var employee = document.getElementById("newEmployee");
       
    
       
        var span2 = document.getElementById("close3");
        
    
        // When the user clicks the button, open the modal
        employee.onclick = function() {
            modalEmployees.style.display = "block";
        }

        span2.onclick = function() {
            modalEmployees.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modalEmployees) {
                modalEmployees.style.display = "none";
            }
        }


        
  


    
    $scope.addUser = function () {

        var User = {
            "U_ID": $scope.U_ID,
            "U_Name": $scope.U_Name,
            "U_LName": $scope.U_LName,
            "U_Phone": $scope.U_Phone,
            "R_ID": 4,
            "U_Password":$scope.U_Password
        }
        console.log(User); 
        $http.post(url+'/api/User/post/',User).
        success(function (data, status, headers, config) {
            alert('User has been posted');
        }).
        error(function (data, status, headers, config) {
            alert('error posting User')
        });
    }
    
   
    

    
       

    
    
}]);

stageForm =  angular.module('admiView')
.controller('stageCtrl', ['$scope', '$http', function ($scope, $http) {
   
     
        // Get the modal
   
        var modalStages = document.getElementById('stageModal');

        
    
        // Get the button that opens the modal
 
        var stage = document.getElementById("newStage");
        
       
    
        
        var span2 = document.getElementById("close2");

        stage.onclick = function() {
            modalStages.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span2.onclick = function() {
            modalStages.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalStages) {
                modalStages.style.display = "none";
            }
        }

     $scope.createStage = function () {
        
         var Stage = {
            "S_Name": $scope.S_Name,
            "S_Description": $scope.S_Description
        
        }
        console.log(Stage);
        
      
        
        $http.post(url+'/api/Stage/post/',Stage).
        success(function (data, status, headers, config) {
            alert('the new Stage has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new Stage')
        });
          
    }
  
 
}]);





