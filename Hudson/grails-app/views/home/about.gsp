<!DOCTYPE html>
<html>
    
    <head>
        <meta name="layout" content="main"/>
        <!-- TODO move css/js into files -->
        <style type="text/css">
        body {
            background-image: url("${resource(dir: 'images/about', file: 'background.jpg')}");
        }
        img {
            max-height: 90%;
        }
        #about-carousel .carousel-control {
            background-image: none;
        }
        </style>
        <script type="text/javascript">
        jQuery(function() {
            var $ = jQuery

            var slideIndex = 1
            var numSlides = 17
            function setSlideIndex(index) {
                var filePrefix = "${resource(dir: 'images/about', file: '')}/"
                $('#carousel-slide').attr('src', filePrefix + index + '.jpg')
            }

            $('#about-carousel').on('slide.bs.carousel', function (event) {
                console.log(event.direction)
                if (event.direction == 'left') {
                    if (slideIndex == numSlides) slideIndex = 1
                    else slideIndex++;
                } else {
                    if (slideIndex == 1) slideIndex = numSlides
                    else slideIndex--;
                }
                setSlideIndex(slideIndex)
                return false
            })
        })
        </script>
        <title>About Hudson</title>
    </head>

    <body>
        <div class="content">
            <div id="about-carousel" class="carousel slide" data-ride="carousel" data-interval="false">
                <!-- Wrapper for slides -->
                <div class="carousel-inner">
                    <div class="item active">
                        <img id="carousel-slide" class="center-block" src="${resource(dir: 'images/about', file: '1.jpg')}" alt="carousel-image">
                    </div>
                    <div class="item">
                        <img class="center-block" src="${resource(dir: 'images/about', file: '2.jpg')}" alt="TODO">
                    </div>
                </div>

                <!-- Controls -->
                <a class="left carousel-control" href="#about-carousel" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                </a>
                <a class="right carousel-control" href="#about-carousel" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                </a>
            </div>
        </div>
    </body>
</html>