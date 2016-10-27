//Vra Globals
var url= 'http://desktop-6upj287:7575';
var userID = localStorage.user;
var userCode = localStorage.code;
/**
 * Modal where the user can sign in
*/
var stageForm = angular.module('clientView',[])
.controller('clientCtrl', ['$scope', '$http', function ($scope, $http) {
        
        //Show the user name
        document.getElementById("idUser").innerHTML = "Welcome "+userID;
     
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


    /**
     * Add the new client to the data base
     * <p>
    */
    $scope.addUser = function () {
        // All information about the new client
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