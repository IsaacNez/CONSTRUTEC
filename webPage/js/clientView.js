var url= 'http://desktop-6upj287:7575';

var stageForm = angular.module('clientView',[])
.controller('clientCtrl', ['$scope', '$http', function ($scope, $http) {
  
     
        // Get the modal
        var modalEmployees = document.getElementById('clientModal');

        
    
        // Get the button that opens the modal
        var employee = document.getElementById("newEmployee");
        
    
        // Get the <span> element that closes the modal
        var span1 = document.getElementById("close1");
       
        
    
        // When the user clicks the button, open the modal
        employee.onclick = function() {
            modalEmployees.style.display = "block";
        }

        span1.onclick = function() {
            modalEmployees.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modalEmployees) {
                modalEmployees.style.display = "none";
            }
        }


        

   
    var rolelist;
    
    $http.get(url+'/api/dbRole/get/R_Name')
        .then( function (response) {
        $scope.rolelist = response.data;
     });


    
    $scope.addUser = function () {

        var User = {
            "U_ID": $scope.U_ID,
            "U_Name": $scope.U_Name,
            "U_LName": $scope.U_LName,
            "U_Phone": $scope.U_Phone,
            "R_ID": 3,
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
