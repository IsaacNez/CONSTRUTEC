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

    
}]);

