<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome page</title>
</head>
<body>
<p>this is the basic welcome/login page</p>
<p><a href="${pageContext.request.contextPath}/newUser.jsp">click here</a> to create an account</p>
<form action="${pageContext.request.contextPath}/loginSV" method="post">
        <label for="email">Email: </label>
        <input type="text" name="email" id="email" value=""><br>
        <label for="password">Password:</label>
        <input type="text" name="password" id="password" value=""><br>
        <input type="submit" name="login" value="log in">
    </form>
</body>
</html>-->


<!DOCTYPE html>
<html lang="en">
<head>
  <title>GUTO</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <link rel='stylesheet' href="Static/styles.css">
  <script type="text/javascript" src="Static/flashImage_v_1_0.js"></script>
    
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top" id="nav_class" >
  <div class="container-fluid">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>                      
      </button>
      <a class="navbar-brand" href="#" style="background-color: gray; color:white;">GuTO</a>
    </div>
      <div class="collapse navbar-collapse" id="myNavbar">
    <ul class="nav navbar-nav">
      <!--<li class="active"><a href="#">Home</a></li>-->
      <li><a href="#services">Services</a></li>
      <li><a href="#contactus">Contact us</a></li>
    </ul>
      
    <ul class="nav navbar-nav navbar-right">
        <li><a href="#" class="" onclick="backToNormal()" data-toggle="modal" data-target="#myModal"> Get started</a></li>
    </ul>
      </div>
  </div>
</nav>
    
    <div class="modal linear" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <center><h4 class="modal-title"><b>Get started</b></h4></center>
        </div>
        <div class="modal-body">
          <ul class="nav nav-tabs">
                    <li class="active jsize"><a data-toggle="tab" href="#prog"><b>Sign in</b></a></li>
                    <li><a data-toggle="tab" href="#scri"><b>Sign up</b></a></li>
                </ul>
            <div class="tab-content">
                    <div id="prog" class="tab-pane fade in active">
                        <center><h4>Sign In</h4></center>
                <form role="form" action="${pageContext.request.contextPath}/loginSV" method="post" name="thisform" class="ng-pristine ng-invalid ng-invalid-required ng-valid-maxlength">
                    <div class="form-group">
                        <label>Email ID</label>
                        <input type="email" name="pass" placeholder="dharitParmar@gmail.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="repass" placeholder="Passw0rd" class="form-control" required="">
                    </div>
                            
                    <center><input type="submit" value="Login" class="btn btn-default btnfrm" onclick="processor()"></center> <br>
                    <center><p id="contact"></p></center>
                </form>  
                    </div>
                    <div id="scri" class="tab-pane fade">
                        <center><h4>Sign Up</h4></center>
                        <form role="form" action="${pageContext.request.contextPath}/newUser.jsp" method="post" name="thisform" class="ng-pristine ng-invalid ng-invalid-required ng-valid-maxlength">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" placeholder="Dharit" name="name" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Email ID</label>
                        <input type="email" name="email" placeholder="dharitParmar@gmail.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="pass" placeholder="Passw0rd" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Re-enter Password</label>
                        <input type="password" name="repass" placeholder="Passw0rd" class="form-control" required="">
                    </div>
                            
                    <center><input type="submit" value="Register" class="btn btn-default btnfrm"></center> <br>
                    <center><p id="contact"></p></center>
                </form>      
                </div>                
            </div>
        </div>
      </div>
    </div>
  </div>
  <!-- boy container-->
<div class="container">
    
    <div class="animateTextProp row row-content">
        <center>
            <h1 class="animateText" style=" ">GuTO</h1>
        </center>
        <center>
            <p class="subTitle">We do one thing and we do it perfectly.</p>
            <p class="subTitle">Which helps business achieve continues business profit.</p>
        </center>
    </div>
    
    <div class="row row-content" >
        <center>
            <center><img class="img-responsive" id="chartAnimation" src="Static/chart/chart0149.png" width="700" height="450" /></center>
        </center>
    </div>
    
    
    <hr/>
    <div id="service_back">
    <div class="row row-content" id="services"> <center> <h3> <b>Services</b> </h3> </center> </div>
    <br/>
    <div class="row row-content">
        <div class="col-xs-12 col-md-3 box-model">
            <center> <h4> <b> <u>BUY </u> </b> </h4> </center>
            <p> Back-end infrastructure for integrating, managing, and securing trading, from any source, at massive scale. and make better decisions. </p>
        </div>
        <div class="col-md-1"></div>
        <div class="col-xs-12 col-md-3 box-model">
            <center> <h4> <b> <u>SELL </u> </b> </h4> </center>
            <p> User interfaces that enable people to interact smoothly with traders, ask better questions, and produces operational market in weeks. </p>
        </div>
        <div class="col-md-1"></div>
        <div class="col-xs-12 col-md-3 box-model">
            <center> <h4> <b> <u> MARKET ANALYSIS </u></b> </h4> </center>
            <p> Proven technology that can be deployed today and adapts to any domain. </p>
        </div>
    </div>
    </div>
    <br/>
    <br/>
    <hr/>
    <div class="row row-content" id="contactus" > <center> <h3> <b>Contact Us</b> </h3> </center>
    <div class="col-xs-12 col-md-4 col-md-offset-4" id="Contactus_form">
        <form role="form" name="thisform" class="ng-pristine ng-invalid ng-invalid-required ng-valid-maxlength">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" placeholder="Dharit" name="name" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Email ID</label>
                        <input type="email" name="email" placeholder="dharitParmar@gmail.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Contact Number</label>
                        <input type="number" name="pass" placeholder="(203) 123-4567" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Message</label>
                        <textarea placeholder="Simple message" class="form-control" required=""> </textarea>
                    </div>
                            
                    <center><input type="submit" value="Contact" class="btn btn-default btnfrm"></center> <br>
                    <center><p id="contact"></p></center>
                </form>
    </div></div>
    <br/>
</div>
    
    <footer class="row-footer">
        <div class="container footerStyle">
        <div class="row row-content">
            <div class="customSetting col-md-4">Copyright © 2017 GuTO Inc. All rights reserved.</div>
            <div class="customSetting col-md-2 col-md-offset-6"><a href="#" class="styleAnchor">Terms and Conditions</a></div>
        </div>
            </div>
    </footer>
    
<script>
    fi = new flashImage(180, 15, "Static/chart/chart", ".png", "chartAnimation", false, true, false, 1, 0, false, 180);
                    fi.preload();
                    fi.flashTest();
</script>

</body>
</html>
