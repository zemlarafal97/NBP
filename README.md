<h1>About</h1>
This is an NBP program which uses <a href="http://api.nbp.pl">NBP Web API</a>  for getting information and analyzing them.


<h1>HELP for NBP Application</h1>

<b>Options:</b>
<ul>
  <li>-goldcurrency       shows currency and gold price</li>
  <b>example:</b> -goldcurrency -d 2017-07-10 -c USD
  
  <li>-goldaverage        shows average gold price between start and end dates</li>
  <b>example:</b> -goldaverage -s 2014-10-10 -e 2016-07-09
  
  <li>-currencyhighestamp shows currency highest amplitude in certain day</li>
  <b>example:</b> -currencyhighestamp -d 2017-10-10
  
  <li>-minpurchaserate    shows currency with minpurchaserate in specified day</li>
  <b>example:</b> -minpurchaserate -d 2016-06-07

  <li>-nsortedcurrencies  shows N sorted currencies sorted increasingly towards difference between sell and purchase price</li>
  <b>example:</b> -nsortedcurrencies -d 2016-07-10 -cur USD,EUR,CHF,GBP

  <li>-currencyminmax     shows for specified currency information when was cheapest/most expensive</li>
  <b>example:</b> -currencyminmax -c USD

  <li>-drawgraph          draws graph for specified currency</li>
   <b>example:</b> -drawgraph -s 2017,01,1 -e 2017,10,2 -c EUR     WHERE 1 and 2 are weeks not days

</ul>



<b>Additional information:</b><br/>
-s     startDate<br/>
-e     endDate<br/>
-c     currencyCode<br/>
-cur   currencies<br/>
-d     date
