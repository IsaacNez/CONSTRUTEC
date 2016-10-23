var indexApp = angular.module('index',[])
.controller('indexCtrl', ['$scope', '$http', function ($scope, $http) {
    var modal = document.getElementById('myModal');
    var loginmodal = document.getElementById('loginModal');

    // Get the button that opens the modal
    var btn = document.getElementById("myBtn");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks the button, open the modal
    btn.onclick = function() {
        modal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
    
    var mensaje = {};
    var roles;
    
    $scope.checkUser = function(){
        
       
         $http.get('http://desktop-6upj287:7575/api/User/get/U_ID,U_Password/'+$scope.U_ID +","+$scope.U_Password)
                .then(function (response) {
                               mensaje=response.data[0];
                               console.log(mensaje);
                                 $scope.roles   = mensaje.q_role; 

                                for(var x = 0; x < $scope.roles.length; x++){
                                    $scope.roles[x] = JSON.parse($scope.roles[x]);
                                }
                                console.log(mensaje.q_id);
                                console.log($scope.roles[0]);
                                
                                for(var x = 0; x < $scope.roles.length; x++){
                                    $scope.roles[x] = JSON.parse($scope.roles[x]);
                                    
                                }
             
              
                                if($scope.roles.length > 1){
                                    
                                    loginmodal.style.display = "block";
                                }
                                else if($scope.roles[0]==3){
                                        console.log("Client");
                                        localStorage.setItem("user", mensaje.q_id); 
                                        window.location.assign("/pages/clientView.html")

                                }
                                else if($scope.roles[0]==1){
                                       console.log("Admin"); 
                                        localStorage.setItem("user", mensaje.q_id); 
                                        window.location.assign("/pages/adminView.html")

                                }          
                                else if($scope.roles[0]==4) {
                                        console.log("General");
                                        localStorage.setItem("user", mensaje.q_id); 
                                        window.location.assign("/pages/generalView.html")
                                }  

                                else if($scope.roles[0]==2) {
                                    console.log("Engineer");
                                      localStorage.setItem("user", mensaje.q_id); 
                                    window.location.assign("/pages/employeeView.html")
                                } 
                                
                 });
        
        
        
        
        
            
                
    }





}]);

indexApp = angular.module('index')
.controller('loginCtrl', ['$scope', '$http', function ($scope, $http) {
    
    
    
    
    $scope.logClient = function(){

                window.location.assign("/pages/clientView.html")        
                 
          
    }
    $scope.logEngineer = function(){

                window.location.assign("/pages/employeeView.html")        
                 
          
    }






}]);