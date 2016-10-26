// GLOBAL VARIABLES TO MANAGE GENERAL INFORMATION
var actualProject;
var actualStage;
var allProducts;
var allAmounts;
var userID = localStorage.user;

//ANGULAR MODULE TO MANAGE THE PROJECT POP POP
var stageForm = angular.module('employeeView',[])
.controller('projectCtrl', ['$scope', '$http', function ($scope, $http)
    {
        document.getElementById("idUser").innerHTML = "Welcome "+userID;
        // VARIABLES TO GET INFORMATION OF DATA BASE
        var Users;
        var Clients;
        $http.get('http://desktop-6upj287:7575/api/Engineer/get/getAll/getAll')
                .then( function (response) {    
                  $scope.Users = response.data;});
        $http.get('http://desktop-6upj287:7575/api/Client/get/getAll/getAll')
                .then( function (response) {    
                  $scope.Clients = response.data;});


        // FUNCTION TO INSERT A NEW PROJECT IN DATA BASE
        $scope.createProject = function (pClient) 
            {
                 var Project = {
                                    "P_Name": $scope.P_Name,
                                    "P_Location": $scope.P_Location,
                                    "U_ID": $scope.U_ID,
                                    "U_code":pClient
                                }
                $http.post('http://desktop-6upj287:7575/api/project/post',Project).
                    success(function (data, status, headers, config) {alert('the new user has been posted!');}).
                    error(function (data, status, headers, config) {alert('Error while posting the new employee')});
             }
    }]);

//ANGULAR MODULE TO MANAGE THE PRODUCT PER STAGE POP POP
stageForm =  angular.module('employeeView')
.controller('stageProductCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
        var productList;
        var prices;

        $scope.getProduct = function (){
            $http.get('http://desktop-6upj287:7575/api/Product/get/PName/'+$scope.PName)
                .then( function (response) { $scope.productList = response.data;}); }
        
        // FUNCTION TO INSERT A PRODUCT 
        $scope.addToCart = function (value1,value2,value3){
            order={
                      "P_ID": actualProject,
                      "S_Name": actualStage,
                      "PR_ID": value1,
                      "PR_Price": value3,
                      "PR_Quantity": value2
                   }
          $http.post('http://desktop-6upj287:7575/api/stagexproduct/post',order).
          success(function (data, status, headers, config) {alert('the new order has been posted!');}).
          error(function (data, status, headers, config)   {alert('Error while posting the new order')});

          if(allProducts=="" && allAmounts==""){
              allProducts=value1;
              allAmounts=value2;         
          }
          else{
              allProducts = allProducts+ "," + value1;
              allAmounts = allAmounts+","  + value2;
          }  
        $scope.addProducts = function(){
            console.log(allProducts);
            console.log(allAmounts);
            
        }
        
    }}]);


//ANGULAR MODULE TO MANAGE THE STAGE PER PROJECT POP POP
stageForm =  angular.module('employeeView')
.controller('stageProjectCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
         $scope.setStage=function(){
         var order;
              order={
                      "P_ID": actualProject,
                      "S_Name": actualStage,
                      "PXS_DateStart":$scope.S_DateStart,
                      "PXS_DateEnd":$scope.S_DateEnd,
                      "PXS_Status": $scope.S_Status,
                    }
              $http.post('http://desktop-6upj287:7575/api/projectxstage/post',order).
              success(function (data, status, headers, config) {alert('the new order has been posted!');}).
              error(function (data, status, headers, config)   {alert('Error while posting the new order')});
            }

}]);


//ANGULAR MODULE TO MANAGE THE STAGE  POP POP
stageForm =  angular.module('employeeView')
.controller('stageCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
     
        // GET MODALS
        var modalEmployees    =  document.getElementById('employeeModal');
        var modalStages       =  document.getElementById('stageModal');
        var modalProject      =  document.getElementById('projectModal');
        var modalStageProject =  document.getElementById('stageProjectModal');

        // BUTTONS ASSIGNED TO MODALS
        var employee     =  document.getElementById("newEmployee");
        var stage        =  document.getElementById("newStage");
        var project      =  document.getElementById("newProject");
        var stageProject =  document.getElementById("addStage");
      
        // SPAN TO CLOSE THE MODALS
        var span1 = document.getElementById("close1");
        var span2 = document.getElementById("close2");
        var span3 = document.getElementById("close3");
        var span4 = document.getElementById("close4");

        // DISPLAY THE MODAL ON CLICK
        employee.onclick     = function() {modalEmployees.style.display = "block";}
        stage.onclick        = function() {modalStages.style.display = "block";}
        project.onclick      = function() {modalProject.style.display = "block";}
        stageProject.onclick = function() {modalStageProject.style.display = "block";}
        
        // QUIT THE MODAL WITH CLICK OUT OF THE MODAL
        window.onclick = function(event) {if (event.target == modalEmployees)    {modalEmployees.style.display = "none";}}
        window.onclick = function(event) {if (event.target == modalStages)       {modalStages.style.display = "none";}}
        window.onclick = function(event) {if (event.target == modalProject)      {modalProject.style.display = "none";}}
        window.onclick = function(event) {if (event.target == modalStageProject) {modalStageProject.style.display = "none";}}
        
        // QUIT THE MODAL WITH CLICK ON X
        span1.onclick = function() {modalEmployees.style.display = "none";}
        span2.onclick = function() {modalStages.style.display = "none";}
        span3.onclick = function() {modalProject.style.display = "none";}
        span4.onclick = function() {modalStageProject.style.display = "none";}

        // FUNTION TO INSERT A NEW STAGE
        $scope.createStage = function () {
             var Stage = {
                            "S_Name": $scope.S_Name,
                            "S_Description": $scope.S_Description
                          }        
            $http.post('http://desktop-6upj287:7575/api/Stage/post/',Stage).
            success(function (data, status, headers, config) {alert('the new Stage has been posted!');}).
            error(function (data, status, headers, config)   {alert('Error while posting the new Stage')});      
        }
      
}]);


//ANGULAR MODULE TO MANAGE THE USER POP POP
stageForm = angular.module('employeeView')
.controller('employeeCtrl', ['$scope', '$http', function ($scope, $http) {
    
    var rolelist;
    var stageList;
    $http.get('http://desktop-6upj287:7575/api/Role/get/R_Name/undefined')
        .then( function (response) {$scope.rolelist = response.data;});
    
    //FUNCTION TO INSERT NEW USER ON DATABASE
    $scope.addUser = function () {

        var User = {
                        "U_ID": $scope.U_ID,
                        "U_Code": $scope.U_Code,
                        "U_Name": $scope.U_Name,
                        "U_LName": $scope.U_LName,
                        "U_Phone": $scope.U_Phone,
                        "R_ID": parseInt($scope.R_ID.substring(0,1)),
                        "U_Password":$scope.U_Password
                    }
        $http.post('http://desktop-6upj287:7575/api/User/post/',User).
        success(function (data, status, headers, config) {alert('User has been posted');}).
        error(function (data, status, headers, config)   {alert('error posting User')});
    }  
}]);



//ANGULAR MODULE TO MANAGE THE PRODUCT PER STAGE POP POP
stageForm =  angular.module('employeeView')
.controller('stageMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
        
     
        var projects; 
        var stagesInProject; 
        var productsInStage; 
        var projectList;
        var stageList; 
        var bigJSON;
        var modalStageMaterial = document.getElementById('stageMaterialModal');
        var modalStageProduct = document.getElementById('stageProductModal');
    
        // GET PROJECTS FROM DB
        $http.get('http://desktop-6upj287:7575/api/Project/get/p_id/undefined')
                .then( function (response) {$scope.projectList = response.data;});

        // FUNCTION TO SET ACTUAL PROJECT AND STAGE WHEN WE WANT TO EDIT 
        $scope.editStage = function(item){
            actualProject = $scope.P_ID;
            console.log("Project/"+actualProject);
            actualStage=item.s_name;
            console.log("Stage/"+actualStage);
        }
        
        // FUNCTION TO SET ACTUAL PROJECT AND STAGE WHEN WE WANT TO EDIT 
        $scope.productStage = function(item){
            actualStage=item.s_name;
            console.log("Stage/"+actualStage);
        }
        
        // FUNCTION TO GET STAGES NAMES 
        $scope.getStages = function (){
            $http.get('http://desktop-6upj287:7575/api/Stage/get/s_name/'+$scope.S_Name)
                .then( function (response) {$scope.stageList = response.data;});}

   
       $scope.updateProject= function(){
           $http.get('http://desktop-6upj287:7575/api/ProjectDetails/get/p_id/'+$scope.P_ID)
                .then( function (response) {$scope.bigJSON = response.data;});
                console.log(bigJSON);
       }
       
        var mensaje2 = {};
    
         // FUNCTION TO GET PROJECT DETAILS FROM DATA BASE 
        $scope.getProjectDetails = function(){
            
             $http.get('http://desktop-6upj287:7575/api/ProjectDetails/get/p_id/'+$scope.P_ID)
                    .then(function (response)
                            {  
                               // CATCH JSON RESPONSE
                               $scope.mensaje2=response.data[0];
                               console.log($scope.mensaje2);
                               $scope.stagesxProject   = $scope.mensaje2.stages; 

                                // SHOW INFORMATION ABOUT THE PROJECT 
                               document.getElementById("name").innerHTML       = "Project Name";
                               document.getElementById("p_name").innerHTML     = $scope.mensaje2.gpd_name;
                               document.getElementById("locate").innerHTML     = "Location";
                               document.getElementById("p_locate").innerHTML   = $scope.mensaje2.gpd_location;
                               document.getElementById("enginner").innerHTML   = "Engineer/Architect";
                               document.getElementById("p_enginner").innerHTML = $scope.mensaje2.gpd_engineer;
                               document.getElementById("owner").innerHTML      = "Owner";
                               document.getElementById("p_owner").innerHTML    = $scope.mensaje2.gpd_owner;  
                               document.getElementById("budget").innerHTML     = "Budget";
                               document.getElementById("p_budget").innerHTML   = $scope.mensaje2.gpd_pbudget;  
                            });
            
        
        }

        $scope.sendOrder = function () {
              var d = new Date();
              var randomOrder = parseInt((Math.floor((Math.random() * 1000000) + 1)* d.getHours())/d.getMinutes()+d.getMilliseconds());
              allProducts = allProducts.replace("undefined,","");
              allAmounts = allAmounts.replace("undefined,",""); 
              order={
                      "O_ID": randomOrder,
                      "Products": allProducts,
                      "Amount":  allAmounts,
                      "OrderDate": d
                    }
              console.log(order);
              $http.post('http://desktop-6upj287:7575/api/Order/post',order).
              success(function (data, status, headers, config) {alert('the new order has been posted!');}).
              error(function (data, status, headers, config)   {alert('Error while posting the new order')});
            }
    
}]);


