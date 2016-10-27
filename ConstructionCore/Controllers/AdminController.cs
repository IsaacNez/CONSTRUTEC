using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Npgsql;
using ConstructionCore.Models;
using System.Data;
using System.Text;
using System.Web.Http.Results;
using System.Web.Http.Cors;
using System.Data.SqlClient;
namespace ConstructionCore.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]

    public class AdminController : ApiController
    {


        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Product>> Get(string attribute, string id)
        {
            NpgsqlConnection myConnection = new NpgsqlConnection();
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "delete from product";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            myConnection.Open();
            command.ExecuteNonQuery();
            myConnection.Close();
            System.Diagnostics.Debug.WriteLine("print5");
            List<Product> values = new List<Product>();
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://desktop-6upj287:7549/");
            client.DefaultRequestHeaders.Accept.Add(new System.Net.Http.Headers.MediaTypeWithQualityHeaderValue("application/json"));

            HttpResponseMessage response = client.GetAsync("api/Product/Get/PR_ID/undefined").Result;
            //string test = "";
            if (response.IsSuccessStatusCode)
            {
                var products = response.Content.ReadAsAsync<IEnumerable<Product>>().Result;
                foreach (var x in products)
                {
                    System.Diagnostics.Debug.WriteLine(x.pr_id);
                    AddProduct(x);
                    values.Add(x);

                }
            }
            else
            {
                System.Diagnostics.Debug.WriteLine("Error while retrieving the products from the database");
            }

            return Json(values);

        }
        public void AddProduct(Product product)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(product.PName);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO product(pr_id,pr_description,pr_name,pr_price,pr_quantity) Values(:pr_id,:pr_description,:pr_name,:pr_price,:pr_quantity)";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");



            command.Parameters.AddWithValue(":pr_id", product.pr_id);
            command.Parameters.AddWithValue(":pr_description", product.PDescription);
            command.Parameters.AddWithValue(":pr_name", product.PName);
            command.Parameters.AddWithValue(":pr_price", product.Price);
            command.Parameters.AddWithValue(":pr_quantity", product.Quantity);

            myConnection.Open();
            command.ExecuteNonQuery();
            System.Diagnostics.Debug.WriteLine("print6");
            myConnection.Close();


        }
    }
}
