const fs = require('fs');
const jsdom = require("jsdom");
const { JSDOM } = jsdom;

const html = fs.readFileSync('src/main/resources/templates/dashboard.html', 'utf8');
const js = fs.readFileSync('src/main/resources/static/js/dashboard.js', 'utf8');

// Mock localStorage/sessionStorage
const dom = new JSDOM(html, {
  url: "http://localhost/",
  runScripts: "dangerously"
});

// Polyfill localStorage
dom.window.localStorage = {
  getItem: function(key) {
    if (key === 'user') return JSON.stringify({ token: 'dummy', id: '123' });
    return null;
  },
  setItem: function() {},
  removeItem: function() {}
};

dom.window.sessionStorage = dom.window.localStorage;
dom.window.atob = function(str) { return Buffer.from(str, 'base64').toString('binary'); };

// Create a dummy token for decodeJWT
const header = Buffer.from(JSON.stringify({alg: "HS256"})).toString('base64');
const payloadObj = {role: "ROLE_ADMIN", name: "Admin", email: "admin@midaxus.com"};
const payload = Buffer.from(JSON.stringify(payloadObj)).toString('base64');
const signature = "dummy";
const dummyToken = `${header}.${payload}.${signature}`;

dom.window.localStorage.getItem = function(key) {
  if (key === 'user') return JSON.stringify({ token: dummyToken, id: '123' });
  return null;
};

try {
  dom.window.eval(js);
  console.log("No errors on load.");
} catch (e) {
  console.error("Runtime error:", e);
}
