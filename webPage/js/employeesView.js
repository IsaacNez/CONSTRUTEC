var actualProject;
var actualStage;
var allProducts;
var allAmounts;
var userID = localStorage.user;

var stageForm = angular.module('employeeView',[])
.controller('projectCtrl', ['$scope', '$http', function ($scope, $http) {
    document.getElementById("idUser").innerHTML = "Welcome "+userID;

    var Users;
    
    // get ingenieers and architects from DB
    $http.get('http://desktop-6upj287:7575/api/Engineer/get/getAll/getAll')
            .then( function (response) {    
              $scope.Users = response.data;           
        });
    var Clients;

    // get ingenieers and architects from DB
    $http.get('http://desktop-6upj287:7575/api/Client/get/getAll/getAll')
            .then( function (response) {    
              $scope.Clients = response.data;           
        });
    var mensaje;
    $http.get('http://desktop-6upj287:7575/api/User/get/U_ID,U_Password/115610679,fockyou')
            .then( function (response) {    
              $scope.mensaje = response.data;           
        });
    console.log(mensaje);


    $scope.createProject = function (pClient) {
         var Project = {
            "P_Name": $scope.P_Name,
            "P_Location": $scope.P_Location,
            "U_ID": $scope.U_ID,
            "U_code":pClient
                        }
        console.log(Project);
        
      
        
        $http.post('http://desktop-6upj287:7575/api/project/post',Project).
        success(function (data, status, headers, config) {
            alert('the new user has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new employee')
        });
        
        
    }

    
}]);
stageForm =  angular.module('employeeView')
.controller('stageProductCtrl', ['$scope', '$http', function ($scope, $http) {
        var url = 'http://isaac:7549';
        var productList;
        var products;
        var amounts; 
        var prices;
    
                             
    
        $scope.getProduct = function (){
            console.log("Updating");
            $http.get('http://desktop-6upj287:7575/api/Product/get/PName/'+$scope.PName)
                .then( function (response) {
                $scope.productList = response.data;
             });
                
        }
        

    

            
            
            
            
        $scope.addToCart = function (value1,value2,value3){
        
          
          if(products=="" && amounts=="" && prices=="" ){
              products=value1;
              amounts=value2;
              prices=value3;
              
          }
          else{
              products = products+ "," + value1;
              amounts = amounts+","  + value2;
              prices = prices+","  + value3  ;
          }
            
          if(allProducts=="" && allAmounts==""){
              allProducts=value1;
              allAmounts=value2;
              
              
          }
          else{
              allProducts = allProducts+ "," + value1;
              allAmounts = allAmounts+","  + value2;
              
          }
         
          allProducts+=products +",";
          allAmounts+= amounts +","; 
          allProducts=allProducts.replace("undefined,", "");
          allAmounts=allAmounts.replace("undefined,", "");
         
           
      }
        $scope.addProducts = function(){
            var order;
            var PRList=products.split(",");
            
            
            var AmountList= amounts.split(",");
            
            for(i = 1; i < PRList.length; i++){
                        order={
                              "P_ID": actualProject,
                              "S_Name": actualStage,
                              "PR_ID": PRList[i],
                              "PR_Price":"",
                              "PR_Quantity": AmountList[i]
                              }
                              console.log(order);
                              $http.post('http://desktop-6upj287:7575/api/stagexproduct/post',order).
                              success(function (data, status, headers, config) {
                                alert('the new order has been posted!');
                               }).
                              error(function (data, status, headers, config) {
                                 alert('Error while posting the new order')
                            });

            }
        }
        
        
  
    
    
    
    
    
    
     
        
    }]);
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
           //   "PXS_Budget":$scope.S_Budget
              }
              console.log(order);
              $http.post('http://desktop-6upj287:7575/api/projectxstage/post',order).
              success(function (data, status, headers, config) {
                alert('the new order has been posted!');
               }).
              error(function (data, status, headers, config) {
                 alert('Error while posting the new order')
            });

            }
        
   /* 
        $scope.setStage = funtion(){
            
        }*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}]);
stageForm =  angular.module('employeeView')
.controller('stageCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
     
        // Get the modal
        var modalEmployees = document.getElementById('employeeModal');
        var modalStages = document.getElementById('stageModal');
        var modalProject = document.getElementById('projectModal');
        var modalStageProject = document.getElementById('stageProjectModal');
        //var modalStageProduct = document.getElementById('stageProductModal');
        
    
        // Get the button that opens the modal
        var employee = document.getElementById("newEmployee");
        var stage = document.getElementById("newStage");
        var project = document.getElementById("newProject");
        var stageProject = document.getElementById("addStage");
        //var stageProduct = document.getElementById("addMaterials");
       
    
        // Get the <span> element that closes the modal
        var span1 = document.getElementById("close1");
        var span2 = document.getElementById("close2");
        var span3 = document.getElementById("close3");
        var span4 = document.getElementById("close4");
      //  var span5 = document.getElementById("close5");
        
    
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
        
        stageProject.onclick = function() {
            modalStageProject.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span4.onclick = function() {
            modalStageProject.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalStageProject) {
                modalStageProject.style.display = "none";
            }
        }
        
       
       




     $scope.createStage = function () {
         console.log("DAAAAAAAAAM");
         var Stage = {
            "S_Name": $scope.S_Name,
            "S_Description": $scope.S_Description
        
        }
        console.log(Stage);
        
      
        
        $http.post('http://desktop-6upj287:7575/api/Stage/post/',Stage).
        success(function (data, status, headers, config) {
            alert('the new Stage has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new Stage')
        });
        
        
     
           
    }
  

    
       
   
   
     
    
}]);



stageForm = angular.module('employeeView')
.controller('employeeCtrl', ['$scope', '$http', function ($scope, $http) {
    
   
    var rolelist;
    var stageList;
    $http.get('http://desktop-6upj287:7575/api/dbRole/get/R_Name')
        .then( function (response) {
        $scope.rolelist = response.data;
     });


    
    $scope.addUser = function () {

        var User = {
            "U_ID": $scope.U_ID,
            "U_Code": $scope.U_Code,
            "U_Name": $scope.U_Name,
            "U_LName": $scope.U_LName,
            "U_Phone": $scope.U_Phone,
            "R_ID": 2,
            "U_Password":$scope.U_Password
        }
        console.log(User); 
        $http.post('http://desktop-6upj287:7575/api/User/post/',User).
        success(function (data, status, headers, config) {
            alert('User has been posted');
        }).
        error(function (data, status, headers, config) {
            alert('error posting User')
        });
    }
    
   
    

    
       

    
    
}]);
stageForm =  angular.module('employeeView')
.controller('stageMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
        
     
       var projects; 
       var stagesInProject; 
       var productsInStage; 
        var projectList;
        var stageList; 
        // Get the modal
        var bigJSON;
        var modalStageMaterial = document.getElementById('stageMaterialModal');
      /*  var stageMaterial =  document.getElementById("addStageMaterials");
        var span6 = document.getElementById("close6");
    
    */
    /////////////////////////////////////////////

        var modalStageProduct = document.getElementById('stageProductModal');
   /*     
        var stageProduct = document.getElementById("addMaterials");
       

        var span5 = document.getElementById("close5");
    
        stageMaterial.onclick = function() {
            modalStageMaterial.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span6.onclick = function() {
            modalStageMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalStageMaterial) {
                modalStageMaterial.style.display = "none";
                
            }
        }
        
        stageProduct.onclick = function() {
            modalStageProduct.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span5.onclick = function() {
            modalStageProduct.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalStageProduct) {
                modalStageProduct.style.display = "none";
            }
        }

  */      
        $scope.editStage = function(item){
            actualProject = $scope.P_ID;
            console.log("Project/"+actualProject);
            actualStage=item.s_name;
            console.log("Stage/"+actualStage);
        }
    
        
        $scope.productStage = function(item){
            actualStage=item.s_name;
            console.log("Stage/"+actualStage);
        }
        
  /*      
        $http.get($scope.url+'/api/dbProject/get/R_Name')
        .then( function (response) {
        $scope.rolelist = response.data;
        });*/
    
    
        $scope.getStages = function (){
            console.log("Updating");
            $http.get('http://desktop-6upj287:7575/api/Stage/get/s_name/'+$scope.S_Name)
                .then( function (response) {
                $scope.stageList = response.data;
             });
            
            
        }

   
            
            $http.get('http://desktop-6upj287:7575/api/Project/get/p_id/undefined')
                .then( function (response) {
                $scope.projectList = response.data;
                ;
             });
    
            console.log(projectList);
    
        
        
       $scope.updateProject= function(){
           $http.get('http://desktop-6upj287:7575/api/ProjectDetails/get/p_id/'+$scope.P_ID)
                .then( function (response) {
                $scope.bigJSON = response.data;
             });
            console.log(bigJSON);
       }
       
        var mensaje2 = {};
        var stagesxProject;
        var productxStages;
        
        var productitos=[];   
        $scope.getProjectDetails = function(){

             $http.get('http://desktop-6upj287:7575/api/ProjectDetails/get/p_id/'+$scope.P_ID)
                    .then(function (response) {
                                  $scope.mensaje2=response.data[0];
                                   console.log($scope.mensaje2);
                                   $scope.stagesxProject   = $scope.mensaje2.stages; 
                 
                                   document.getElementById("name").innerHTML = "Project Name";
                                   document.getElementById("p_name").innerHTML = $scope.mensaje2.gpd_name;
                 
                                   document.getElementById("locate").innerHTML = "Location";
                                   document.getElementById("p_locate").innerHTML = $scope.mensaje2.gpd_location;
                           
                                   document.getElementById("enginner").innerHTML = "Engineer/Architect";
                                   document.getElementById("p_enginner").innerHTML = $scope.mensaje2.gpd_engineer;
                 
                                   document.getElementById("owner").innerHTML = "Owner";
                                   document.getElementById("p_owner").innerHTML = $scope.mensaje2.gpd_owner;  
                                        
                                    
                                  /*  for(var x = 0; x < $scope.stagesxProject.length; x++){
                                        console.log("here1");
                                        console.log($scope.stagesxProject);
                                        
                                        console.log("here2");
                                        $scope.productxStage = $scope.stagesxProject[x].products;
                                        console.log($scope.productxStage);*/
                                        
                                /*        for(var y = 0; y < $scope.productxStage.length; y++){
                                            var table = document.getElementById("cart");
                                            var row = table.insertRow(0);
                                            var cell1 = row.insertCell(0);
                                            var cell2 = row.insertCell(1);
                                            var cell3 = row.insertCell(2);
                                            var cell4 = row.insertCell(3);

                                            cell1.innerHTML = productxStage[y].p_id;
                                            cell2.innerHTML = productxStage[y].p_id;
                                            cell3.innerHTML = "NEW CELL1";
                                            cell4.innerHTML = "NEW CELL2";
                                        } */
                        
                                        
                          
                                    
                                   
                                        
                                      console.log(productitos);
                 
                                    
  
                                    console.log(mensaje2.gpd_name);
                                    console.log(mensaje2.gpd_location);
                                    console.log(mensaje2.gpd_enginner);
                                    console.log(mensaje2.gpd_owner);  
                                        
                                    }
                                    
                                    
                 
          );
            
        
        }
   /*    $scope.addStage = function () {
         console.log("DAAAAAAAAAM");
         var Stage = {
            "S_Name": actualStage,
            "S_Description": $scope.S_Description,
            "S_DateStart": $scope.S_DateStart,
            "S_DateEnd": $scope.S_DateEnd,
            "S_Status": $scope.S_Status,
            "S_Budget": 0  
        }
        console.log(Stage);
        
      
        
        $http.post(http://desktop-6upj287:7575/api/St/api/dbStage/post/',Stage).
        success(function (data, status, headers, config) {
            alert('the new Stage has been posted!');
        }).
        error(function (data, status, headers, config) {
            alert('Error while posting the new Stage')
        });
        
        
    } */  
    
        $scope.sendOrder = function () {
            var d = new Date();
            var randomOrder = parseInt((Math.floor((Math.random() * 1000000) + 1)* d.getHours())/d.getMinutes()+d.getMilliseconds());
            console.log(randomOrder);
            
          order={
              "O_ID": randomOrder,
              "Products": allProducts,
              "Amount": allAmounts,
              "OrderDate": d
              
              
          }
          console.log(order);
          $http.post('http://desktop-6upj287:7575/api/Order/post',order).
          success(function (data, status, headers, config) {
            alert('the new order has been posted!');
           }).
          error(function (data, status, headers, config) {
             alert('Error while posting the new order')
        });
        }
        
  

    
       
   
   
     
    
}]);


