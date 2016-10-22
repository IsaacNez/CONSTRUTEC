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
namespace ConstructionCore.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class StagexproductController : ApiController
    {
        [HttpPost]
        [ActionName("Post")]
        public void Addproducts(stagexproduct stage)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(stage.s_name);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO stagexproduct(s_name,pr_id,p_id,pr_price,pr_quantity) Values(:s_name,:pr_id,:p_id,:pr_price,:pr_quantity)";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");
            System.Diagnostics.Debug.WriteLine("generando comando");
            try
            {
                command.Parameters.AddWithValue(":p_id", stage.p_id);
                command.Parameters.AddWithValue(":s_name", stage.s_name);
                command.Parameters.AddWithValue(":pr_id", stage.pr_id);
                command.Parameters.AddWithValue(":pr_price", stage.pr_price);
                command.Parameters.AddWithValue(":pr_quantity", stage.pr_quantity);

                myConnection.Open();
                command.ExecuteNonQuery();

                System.Diagnostics.Debug.WriteLine("print6");
                myConnection.Close();
            }
            catch (Npgsql.PostgresException)
            {
                System.Diagnostics.Debug.WriteLine("print6");

            }
        }
    }
}
