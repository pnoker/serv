(function($){
    $.fn.validationEngineLanguage = function(){
    };
    $.validationEngineLanguage = {
        newLang: function(){
            $.validationEngineLanguage.allRules = {
                "onlyNumberSp": {
                    "regex": /^[0-9\ ]+$/,
                    "alertText": " 只能填数字"
                }
            };

        }
    };
    $.validationEngineLanguage.newLang();
})(jQuery);
