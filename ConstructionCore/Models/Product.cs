using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class Product
    {
        public int pr_id { get; set; }
        public string PDescription { get; set; }
        public string PName { get; set; }
        public int Price { get; set; }
        public int Quantity { get; set; }
    }
}