const express = require("express");
const bodyParser = require("body-parser");
const bcrypt = require("bcryptjs");
const passport = require("passport");
const BasicStrategy = require("passport-http").BasicStrategy;
const jwt = require("jsonwebtoken");
const jwtStrategy = require("passport-jwt").Strategy,
  ExtractJwt = require("passport-jwt").ExtractJwt;
const jwtSecretKey = require("./jwt-key.json");
const e = require("express");
const app = express();
const port = 3000;

app.use(bodyParser.json());

// Object arrays for development before database
let users = [];
let stories = [];

/*********************************************
 * AUTHENTICATION STRATEGIES
 ********************************************/

// Http basic authentication strategy
passport.use(
  new BasicStrategy(function (username, password, done) {
    // find user from resources by username
    const user = users.find((e) => e.username == username);

    // if user not found
    if (user == undefined) {
      return done(null, false, { message: "HTTP Basic username not found" });
    }

    // compare passwords
    if (bcrypt.compareSync(password, user.password) == false) {
      return done(null, false, { message: "HTTP Basic password not found" });
    }

    return done(null, user);
  })
);
  
// JWT authentication strategy
let options = {};

options.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
options.secretOrKey = jwtSecretKey.secret;

passport.use(
  new jwtStrategy(options, function (jwt_payload, done) {
    // test if the token is expired
    const now = Date.now() / 1000;

    if (jwt_payload.exp > now) {
      done(null, jwt_payload.user);
    } else {
      done(null, false);
    }
  })
);

/*********************************************
 * USER ENDPOINTS
 ********************************************/

app.post("/user/register", (req, res) => {
  // tests that request body has required data
  if ("username" in req.body == false) {
    res.status(400).send("Bad Request: Missing username");
    return;
  }
  if ("password" in req.body == false) {
    res.status(400).send("Bad Request: Missing password");
    return;
  }
  if ("email" in req.body == false) {
    res.status(400).send("Bad Request: Missing email");
    return;
  }

  //TODO: Test that username is not taken

  // hash the password
  const hashedPassword = bcrypt.hashSync(req.body.password, 6);

  const newUser = {
    id: users.length + 1,
    username: req.body.username,
    email: req.body.email,
    password: hashedPassword,
  };
  users.push(newUser);

  res.status(201).json({ newUser });
});

app.get("/user/login",
  passport.authenticate("basic", { session: false }),
  (req, res) => {
    // construct body and set options
    const body = {
      id: req.user.id,
      username: req.user.username,
      email: req.user.email,
    };

    const payload = {
      user: body,
    };

    const options = {
      expiresIn: "600s",
    };

    // create and return token
    const token = jwt.sign(payload, jwtSecretKey.secret, options);
    res.status(200).json({ token });
  }
);

// get users for testing
app.get("/user", (req, res) => {
  res.json({ users });
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})