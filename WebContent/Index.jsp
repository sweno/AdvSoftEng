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
    <link rel="stylesheet" href="Static/Text_Demo1.css">
    <link rel="stylesheet" type="text/css" href="Static/style2.css" />
    <link rel="stylesheet" type="text/css" href="Static/CustomerReview.css"/>
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
      <a class="navbar-brand" href="#" ><img src="Static/logo2.png" width="60px" height="35px" /></a>
    </div>
      <div class="collapse navbar-collapse" id="myNavbar">
    <ul class="nav navbar-nav">
      <!--<li class="active"><a href="#">Home</a></li>-->
      <li><a href="#services">SERVICES</a></li>
      <li><a href="#contactus">CONTACT US</a></li>
      <li><a href="#c_r">CUSTOMER REVIWS</a></li>
    </ul>
      
    <ul class="nav navbar-nav navbar-right">
        <li><a href="#" class="" onclick="backToNormal()" data-toggle="modal" data-target="#myModal">GET STARTED</a></li>
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
                <form role="form" action="${pageContext.request.contextPath}/login" name="thisform" class="ng-pristine ng-invalid ng-invalid-required ng-valid-maxlength" method="post">
                    <div class="form-group">
                        <label>Email ID</label>
                        <input type="email" name="email" placeholder="dharitParmar@gmail.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" placeholder="Passw0rd" class="form-control" required="">
                    </div>
                            
                    <center><input type="submit" value="Login" class="btn btn-default btnfrm" onclick="processor()"></center> <br>
                    <center><p id="contact"></p></center>
                </form>  
                    </div>
                    <div id="scri" class="tab-pane fade">
                        <center><h4>Sign Up</h4></center>
                        <form role="form"  action="${pageContext.request.contextPath}/newUser.jsp" name="thisform" class="ng-pristine ng-invalid ng-invalid-required ng-valid-maxlength" method="post">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" placeholder="User" name="name" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Email ID</label>
                        <input type="email" name="email" placeholder="user@example.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="pass" placeholder="Password" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Re-enter Password</label>
                        <input type="password" name="repass" placeholder="Password" class="form-control" required="">
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
  <!-- body container-->
<div class="container">
    
    <div class="animateTextProp row row-content">
        <center>
            <h1 class="animateText" style=" ">GuTO</h1>
        </center>
        
    </div>
    
    <div class="row row-content" >
        <center>
            <center><img class="img-responsive" id="chartAnimation" src="Static/chart/chart0149.png" width="700" height="450" /></center>
        </center>
    </div>
    
    
    <hr/>
    <div id="service_back">
           <div class="rw-wrapper" id="services">
				<h2 class="rw-sentence">
                    <span>We do one thing and we do it</span>
                    
					<div class="rw-words rw-words-1">
						<span>Perfectly</span>
						<span>with continuous efforts</span>
						<span>having one goal in mind</span>
					</div>
					<br />
					<span>Which helps business achieve</span>
					<div class="rw-words rw-words-2">
						<span>continues business profit.</span>
                        <span>global present.</span>
						<span>steady growth.</span>
						<span>superior market analysis</span>
					</div>
				</h2>
            </div>
    <div class="row row-content" style="">
        <div class="col-xs-12 col-md-3 box-model" style="background-color:black; opacity: 0.9; color: white;">
            <center> <h4> <b> <u>BUY </u> </b> </h4> </center>
            <p><strong> Back-end infrastructure for integrating, managing, and securing trading, from any source, at massive scale. and make better decisions.</strong> </p>
        </div>
        <div class="col-md-1"></div>
        <div class="col-xs-12 col-md-3 box-model" style="background-color:black; opacity: 0.9; color: white;">
            <center> <h4> <b> <u>SELL </u> </b> </h4> </center>
            <p> User interfaces that enable people to interact smoothly with traders, ask better questions, and produces operational market in weeks. </p>
        </div>
        <div class="col-md-1"></div>
        <div class="col-xs-12 col-md-3 box-model" style="background-color:black; opacity: 0.9; color: white;">
            <center> <h4> <b> <u> MARKET ANALYSIS </u></b> </h4> </center>
            <p> Proven technology that can be deployed today and adapts to any domain. </p>
        </div>
    </div>
    </div>
    <br/>
    <br/>
    <hr/>
    <div class="review" id="c_r">
         <center><h2>Who we are! That our customers discribe better than us</h2></center>
            <div id="" class="carousel slide text-center" data-ride="carousel">
              <!-- Indicators -->
                  <ol class="carousel-indicators">
                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li>
                  </ol>

                  <!-- Wrapper for slides -->
                  <div class="carousel-inner" role="listbox">
                    <div class="item active">
                    <h4>"This is not a trading platform. it is a market, which establish my organization I am so happy with the result!"<br><span style="font-style:normal;">Terry Guldimann, Vice President, Healthy Net Security</span></h4>
                    </div>
                    <div class="item">
                      <h4>"TWo word... Consistent Profits! isn't it enough! "<br><span style="font-style:normal;">Dharit Parmar, CTO, DP Inc</span></h4>
                    </div>
                    <div class="item">
                      <h4>"Global reach is on our finger tip. with reduced expenses for marketing".  <br><span style="font-style:normal;">Gaurav Kumar, - Marketing and sales, YaraSccan Machines</span></h4>
                    </div>
                      <div class="item">
                      <h4>"We are guided by 'GuTO' that is our quality ".  <br><span style="font-style:normal;">Harry Patel, Harry Design PVT LTD</span></h4>
                    </div>
                  </div>
                  <!-- Left and right controls -->
                  <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                  </a>
                  <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                  </a>
            </div>
    </div>
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
                        <input type="email" name="email" placeholder="dharit@gmail.com" class="form-control" required="">
                    </div>

                    <div class="form-group">
                        <label>Contact Number</label>
                        <input type="number" name="pass" placeholder="(203) 123-4007" class="form-control" required="">
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
