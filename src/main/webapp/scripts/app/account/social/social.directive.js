'use strict';

angular.module('jhipsterApp')
    .directive('jhSocial', function($cookies) {
        // these link to functionality provided by spring-social
        var scopes = {
            'facebook': 'public_profile,email',
            'google': 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
        };
        return {
            restrict: 'E',
            replace: true,
            transclude: true,
            link: function(scope, element, attrs, ctrl, transclude) {
                scope.$watch(attrs['externalAccountProvider'],
                    function(externalAccountProvider) {
                        if (externalAccountProvider !== undefined) {
                            // turn the enumerated value into something more javascript/css friendly
                            var clientSideProviderName = externalAccountProvider.toLowerCase();
                        	
                            // build either a block style or icon style button
                            var type = attrs['type'];
                            var btn;
                            if (type === 'icon') {
                            	btn = element;
                            	btn.addClass('btn-social-icon btn-sm');
                            } else {
                            	// set up form
                                element.attr('action', '/signin/' + clientSideProviderName);
                                element.find('input[name="scope"]').val(scopes[clientSideProviderName]);
                                element.find('input[name="_csrf"]').val($cookies.get('CSRF-TOKEN'));

                            	btn = element.find('button');
                            	btn.addClass('btn-social');
                            }
                            if (type === 'block') {
                            	btn.addClass('btn-block');
                            }

                            // add classes common to both block and button styles.  for example, "btn-facebook" and "fa-facebook".
                            btn.addClass('btn-' + clientSideProviderName);
                            if (clientSideProviderName === 'google') {
                                // workaround for google
                            	btn.find('i').addClass("fa-google-plus");
                            } else {
                            	btn.find('i').addClass("fa-" + clientSideProviderName);
                            }
                            
                            // include whatever content the caller included in our element
                            btn.append(transclude());
                        }
                    }
                );
            },
            template:function(element, attrs){
            	if (attrs['type'] === 'icon') {
            		return '<a class="btn"><i class="fa"></i></a>';
            	} else {
            		return '<form method="POST" action="#">' +
	            		'<button class="btn" type="submit"><i class="fa"></i></button>' +
	            		'<input name="scope" type="hidden" />' +
	            		'<input name="_csrf" type="hidden" />' +
	            		'</form>';
            	}
            }
        }
     });
