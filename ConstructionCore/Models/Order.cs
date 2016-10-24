using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class Order
    {
        public int OPriority { get; set; }
        public string OStatus{get;set;}
        public DateTime OrderDate { get; set; }
        public int S_ID = 1;
        public int C_ID = 123;
        public int O_ID { get; set; }
    }
}