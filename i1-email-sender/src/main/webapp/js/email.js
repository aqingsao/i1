function EmailsCtrl($scope) {
    $scope.emails = [];
    $scope.addEmail = function(email){
        console.log("add email");
        $scope.emails.push(email);

        $scope.emails.sort(function(a, b){return b.time - a.time});
    };
    $.get("api/email", function(emails){
        for(var i in emails){
            $scope.$apply(function(){
                $scope.addEmail(toEmail(emails[i]));
            });
        }
    });

    var toEmail = function(email){
        var recipients = [];
        for(var i in email.recipients.emailRecipientsTo){
            recipients.push(email.recipients.emailRecipientsTo[i].address.userAddress);
        }
        for(var i in email.recipients.emailRecipientsCC){
            recipients.push(email.recipients.emailRecipientsCC[i].address.userAddress);
        }
        for(var i in email.recipients.emailRecipientsBCC){
            recipients.push(email.recipients.emailRecipientsBCC[i].address.userAddress);
        }
        var result = 'ERROR';
        switch(email.status){
            case 'SENDING':
                result = 'warning';
                break;
            case 'ERROR':
                result='error';
                break;
            case 'SUCCESS':
                result='success';
                break;
        }
        return {sender:email.sender.from.userAddress,
            subject:email.subject,
            message:email.message,
            recipients: recipients.join(", "),
            time: email.updatedAt,
            result: result};
    }
}













