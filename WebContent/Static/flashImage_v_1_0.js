function flashImage(totImages, speed, imgName, imgFormat, imageId, playFEver, loop, prestart, loopTimes, reverseSpeed, reverseAnim, defImage)
{
    this.num = totImages;
    
    var fnum = totImages;
    
    var n = this.num;
    
    this.name = imgName;
    
    this.ext = imgFormat;
    
    var idName = imageId;
    
    var playEver = playFEver;
    
    var imgs = new Array();
    
    var defImg = defImage;
    
    var lrepeat = loop;
    
    var counter = 0;
    
    var loopNum = loopTimes * n;
    
    var date = new Date();
    
    var recSec = 0;
    
    var sec = undefined;
    
    var counter2 = 0;
    
    var imgNum = 1;
    
    var revAnimSpeed = parseInt(1000/reverseSpeed);
    
    var revAnim = reverseAnim;
    
    var istatus = false;
    
    var lstatus = false;
    
    var tna = arguments.length;
    
    var distatus = false;
    
    const perception = parseInt(1000/speed);
    
    var flashMovie = undefined;
    
    if (playEver) {
        playForever();
    }
    
    if(arguments[tna-1] >= 1 && arguments[tna-1] <= (this.num+1) && typeof arguments[tna-1] == "number") {
        distatus = true;
        defImg = arguments[tna-1];
    }
    
    this.preload = function preload() {
        
        if (defImg > n)
            this.num = n+1;
        
        for(var i = 1; i<=this.num; i++)
        {
            imgs[i] = new Image();
                    
            if(i < 10)
                imgs[i].src = this.name + "000" + i + this.ext;
            else if(i >= 10 && i < 100)
                imgs[i].src = this.name + "00" + i + this.ext;
            else if(i >= 100 && i < 1000)
                imgs[i].src = this.name + "0" + i + this.ext;
            else
                imgs[i].src = this.name + i + this.ext;
        }   
    }
            
    this.flashTest = function flashTest() {
        
        
        if(!playEver) {
            
        // Counter to check user's choice
        if(!lstatus)
            counter++;
        else
            lstatus = false;
            
        
        if(prestart)
        {
            imgNum = 1;
            clearInterval(flashMovie);
        }   
        
        if(istatus)
        {
            if(counter%2 == 0)
                counter++;
            imgNum = 1;
            istatus = false;
            clearInterval(flashMovie);
        }
        
        if(counter%2 != 0)
            flashMovie = setInterval(function() {
                    if(imgNum == n+1 && revAnim && lrepeat)
                    {
                        clearInterval(flashMovie);
                        flashMovie = setInterval(function() {
                            imgNum--;
                            if(imgNum == 1)
                            {
                                clearInterval(flashMovie);
                                lstatus = true;
                                flashTest();
                            }
                            document.getElementById(idName).src = imgs[imgNum].src;
                        }, revAnimSpeed);
                    }
                    else if(imgNum == n+1 && lrepeat)
                        imgNum = 1;
                    else if(imgNum == n+1)
                    {
                        clearInterval(flashMovie);
                        istatus = true;
                        if(distatus)
                            document.getElementById(idName).src = imgs[defImg].src;
                        else
                            document.getElementById(idName).src = imgs[parseInt(n/2)].src;
                    }
                    else
                    {
                        document.getElementById(idName).src = imgs[imgNum].src;
                        imgNum++;
                    }
                
                    if(loopNum > 0) {
                        loopNum--;
                        if(loopNum == 0)
                            lrepeat = false;        
                    }
                
                    }, perception);    
                
        else
        {
            clearInterval(flashMovie);
            if(distatus)
                document.getElementById(idName).src = imgs[defImg].src;
        }
        }
    }
    
    this.resetAnim = function resetAnim() {
        clearInterval(flashMovie);
    }
    
    function playForever() {
        playEver = true;
        flashMovie = setInterval(function() {
                imgNum++;
                if(imgNum == fnum)
                    imgNum = 1;
                
                document.getElementById(idName).src = imgs[imgNum].src;   
            }, perception);  
    }
}