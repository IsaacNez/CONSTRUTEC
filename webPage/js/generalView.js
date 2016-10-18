var index;
function  getIndex(element) {
    alert(" row " + element.rowIndex);
    index = element.rowIndex;
    var temp = document.getElementById("tableStage").rows[index].cells[0].innerHTML;
    alert(temp);
    alert(temp.substring(temp.lastIndexOf("<b>")+1,temp.lastIndexOf("</b>")));
    alert(document.getElementById("tableStage").rows[index].cells[0].innerHTML);
    alert(document.getElementById("tableStage").rows[0].cells[0].innerHTML);
    
}
var stageForm = angular.module('generalView',[])
.controller('projectCtrl', ['$scope', '$http', function ($scope, $http) {
    
    var url = 'http://desktop-6upj287:7249';
    // get ingenieers and architects from DB
    var Users;

    // get ingenieers and architects from DB
    $http.get($scope.url+'/api/dbUser/undefined')
            .then( function (response) {    
              $scope.Users = response.data;           
        });
    
   
}]);

stageForm =  angular.module('generalView')
.controller('projectMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
     
       
        
        // Get the modal

        var modalProjectMaterial = document.getElementById('projectMaterialModal');
        var projectMaterial = document.getElementById("projectMaterial");
        var span1 = document.getElementById("close1");
    
        projectMaterial.onclick = function() {
            modalProjectMaterial.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span1.onclick = function() {
            modalProjectMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalProjectMaterial) {
                modalProjectMaterial.style.display = "none";
                
            }
        }
       
        
        $http.get($scope.url+'/api/Project/get')
        .then( function (response) {
        $scope.projectlist = response.data;
        });
    
    
        $scope.getProjectByMaterial= function (){
            $http.get($scope.url+'/api/Project/get/P_Name/' + $scope.S_Name)
            .then( function (response) {
            $scope.projectlist = response.data;
            });
        }

       
              
       
}]);



stageForm =  angular.module('generalView')
.controller('stageMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
     
       
        
        // Get the modal

        var modalStageMaterial = document.getElementById('stageMaterialModal');
        var stageMaterial = document.getElementById("addStageMaterial");
        var span2 = document.getElementById("close2");
    
        stageMaterial.onclick = function() {
            modalStageMaterial.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span2.onclick = function() {
            modalStageMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalStageMaterial) {
                modalStageMaterial.style.display = "none";
                
            }
        }
        
        $http.get($scope.url+'/api/Project/get/P_Name')
        .then( function (response) {
        $scope.ProjectList = response.data;
        });
    
    
        $scope.getStages = function (){
            console.log("Updating");
            $http.get('http://desktop-6upj287:7249/api/stage/get/'+$scope.S_Name)
                .then( function (response) {
                $scope.stageList = response.data;
             });
        }

       
              
       
}]);

stageForm =  angular.module('generalView')
.controller('productCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
     
       
        
        // Get the modal

        var product = document.getElementById('productModal');
        var span3 = document.getElementById("close3");
    
       

        // When the user clicks on <span> (x), close the modal
        span3.onclick = function() {
            product.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == product) {
                product.style.display = "none";
                
            }
        }
        
     $http.get($scope.url+'/api/Product/get')
                .then( function (response) {    
                  $scope.productlist = response.data;           
            });
      
}]);

stageForm =  angular.module('generalView')
.controller('commentCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
     
       
        
        // Get the modal

        var modalcommentMaterial = document.getElementById('commentModal');
        var span4 = document.getElementById("close4");
    
       

        // When the user clicks on <span> (x), close the modal
        span4.onclick = function() {
            modalcommentMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalcommentMaterial) {
                modalcommentMaterial.style.display = "none";
                
            }
        }
        
     $http.get($scope.url+'/api/dbComment/get')
                .then( function (response) {    
                  $scope.commentlist = response.data;           
            });

        $scope.addComment = function () {
            var Comment = { 
                "C_Name": $scope.C_Name,
                "C_Description": $scope.C_Description,
                

            }
            console.log(Comment); $http.post('http://isaac:7549/api/dbComment/post',Comment).
            success(function (data, status, headers, config) {
                alert('Comment has been posted');
            }).
            error(function (data, status, headers, config) {
                alert('error adding comment')
            });
        }


       
              
       
}]);
