var stageForm = angular.module('clientView',[])
.controller('productCtrl', ['$scope', '$http', function ($scope, $http) {
    var url = 'http://isaac:7549';
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
            modalProducts.style.display = "block";
        }

        span1.onclick = function() {
            modalProducts.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modalProducts) {
                modalProducts.style.display = "none";
            }
        }


    // get all products from the EPA-TEC core
    $scope.getProducts = function () {        
       $http.get($scope.url+'/api/product/get')
            .then( function (response) {
            $scope.productList = response.data;
         });
       
        success(function (data, status, headers, config) {
            
            alert(JSON.stringify(data));
            console.log(JSON.stringify(data));
        }).
        error(function (data, status, headers, config) {
            alert('error searching product')
        });   
        
       
        
        
    }
    
    $http.get($scope.url+'/api/dbComment/get')
            .then( function (response) {    
              $scope.commentlist = response.data;           
        });
    
    $scope.addComment = function () {
        var Comment = { 
            "C_Name": $scope.C_Name,
            "C_Description": $scope.C_Description,
            "S_Name": 1
      
        }
        console.log(Comment); $http.post('http://isaac:7549/api/dbComment/post',Comment).
        success(function (data, status, headers, config) {
            alert('Comment has been posted');
        }).
        error(function (data, status, headers, config) {
            alert('error adding comment')
        });
    }
    
        // get all products from the EPA-TEC core
    $scope.getComments = function () {        
       $http.get($scope.url+'/api/dbComment/get')
            .then( function (response) {
            $scope.commentlist = response.data;
         });
       
        success(function (data, status, headers, config) {
            
            alert(JSON.stringify(data));
            console.log(JSON.stringify(data));
        }).
        error(function (data, status, headers, config) {
            alert('error searching comments')
        });   
        
       
        
        
    }

    
}]);

