var stageForm = angular.module('employeeView',[])
.controller('projectCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
    // get ingenieers and architects from DB
    var Users;

    // get ingenieers and architects from DB
    $http.get($scope.url+'/api/user/undefined')
            .then( function (response) {    
              $scope.Users = response.data;           
        });
    
/*    var str = $scope.S_List; 
    var str1= "stage1,stage2,stage3"
    var prueba= str1.split(",");
    console.log(prueba);   
    var cuisines = ["Chinese","Indian"];     

    var sel = document.getElementById('list');
    for(var i = 0; i < cuisines.length; i++) {
        var opt = document.createElement('h2');
        opt.setAttribute("data-toggle","collapse");
        opt.setAttribute("data-parent","#accordion");
        opt.innerHTML = cuisines[i];
        opt.value = cuisines[i];
        sel.appendChild(opt);
    }
    console.log(prueba);  
    */

    $scope.createProject = function () {
         var Project = {
            "P_ID": $scope.P_ID,
            "P_Name": $scope.P_Name,
            "P_Location": $scope.P_Location,
            "P_Budget": 0,
            "U_Code": $scope.U_Code,
            "U_ID": $scope.U_ID
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

    
}]);

stageForm =  angular.module('employeeView')
.controller('stageCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
     
        // Get the modal
        var modalEmployees = document.getElementById('employeeModal');
        var modalStages = document.getElementById('stageModal');
        var modalProject = document.getElementById('projectModal');

        // Get the button that opens the modal
        var employee = document.getElementById("newEmployee");
        var stage = document.getElementById("newStage");
        var project = document.getElementById("newProject");

        // Get the <span> element that closes the modal
        var span1 = document.getElementById("close1");
        var span2 = document.getElementById("close2");
        var span3 = document.getElementById("close3");

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

        project.onclick = function() {
            modalProject.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span3.onclick = function() {
            modalProject.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalProject) {
                modalProject.style.display = "none";
            }
        }




     $scope.createStage = function () {
         console.log("DAAAAAAAAAM");
         var Stage = {
            "S_Name": $scope.S_Name,
            "S_Description": $scope.S_Description,
            "S_DateStart": $scope.S_DateStart,
            "S_DateEnd": $scope.S_DateEnd,
            "S_Status": $scope.S_Status,
            "S_Budget": 0
            
        }
        console.log(Stage);
        
      
        
        $http.post($scope.url+'/api/stage/post/',Stage).
        success(function (data, status, headers, config) {
            alert('the new Stage has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new Stage')
        });
        
        
    }
  

    
       
   
   
     
    
}]);
