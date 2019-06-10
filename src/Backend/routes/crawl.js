/* This route is used for testing in the development environment */
var express = require('express');
var router = express.Router();
const puppeteer = require('puppeteer');

var browser;

let initBrowser = async () => {
  browser = await puppeteer.launch();
}

let crawl = async (req)=> {
  const page = await browser.newPage();

  console.log("Accessing page...")

  var i;
  switch (req.params.site) {
    case "topman":
      await page.goto('https://www.topman.com/en/tmuk/category/clothing-140502');
      i = await page.evaluate((interval, count) => {
          return new Promise((resolve, reject) => {
              (async () => {
                  await new Promise(function(resolve, reject) {
                      let intervalID = setInterval(() => {
                          scrollTo(0,document.body.scrollHeight);
                          setTimeout(() => {
                              scrollTo(0,0);
                          }, interval / 2);
                      }, interval);
                      setTimeout(()=> {
                          clearTimeout(intervalID);
                          resolve();
                      }, interval * count);
                  });

                  console.log("processing page...");
                      
                  let products = document.querySelectorAll("div.Product");
                  let productList = [];
                  products.forEach(product => {
                      try {
                          productList.push({
                              item_name: product.querySelector('.Product-name').innerText,
                              item_url: product.querySelector('a.Product-link').href,
                              image_url: product.querySelector('img.ProductImages-image').src
                          });
                      } catch (error) {
                          
                      }                  
                  });

                  scrollTo(0, document.body.scrollHeight);
                  resolve(productList);
              })();
          });
      }, 1000, 20);
      break;
    case "zara":
      await page.goto("https://www.zara.com/kr/en/woman-trousers-shorts-l1355.html?v1=1180630")
      i = await page.evaluate(() => {
        return new Promise(resolve => {
          let n = document.body.scrollHeight / 200;
          let i = 0;
          setInterval(function() {
            scrollBy(0, 200);
            if ((i++) > n) {
              var data = [];
              document.querySelectorAll("li.product").forEach(a=> {
                var i = a.querySelector("div.product-info-item>a._item");
                var im = a.querySelector("img.product-media");
                data.push(
                  {
                    itemName: i.innerText, itemUrl: i.href, imageUrl: im.src
                  }
                );
              });
              resolve(data);
            }
          }, 100);
        });
      });
      break;
  }
  console.log(i);

  await page.close();

  req.db.ref('/items/' + req.params.site).once("value", function(data) {
    console.log(data.toJSON());
    i.forEach(item=> {
      data.ref.push().set(item, function(error){
        if (error){
          console.log(error);
        }
      });
    });
  });
};

initBrowser();

/* GET dev index page page. */
router.get('/', function(req, res, next) {
  res.render('crawl', { title: 'Item crawler' });
});

/* Get test route. */
router.get('/:site', function (req, res) {
  // Database is defined in req.db
  // The collection /TestMessages is referenced and TestMessage variable inside
  // is assigned value defined in the object inside the set() paramater.

  if (browser) {
    crawl(req);
  }
  res.send(browser? "Crawl started": "Browser not initiated");
});

module.exports = router;