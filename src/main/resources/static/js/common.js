$(document).ready(function () {
    $('#logoutLink').on('click', function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });
    showDropdown();
});

function clearSearch() {
    window.location = moduleURL;
}

// Thêm dấu "." vào sau mỗi 3 số VD: 3.000.000 VNĐ
function addCommasToNumber(number) {
    let reversedNumber = String(number).split('').reverse().join('');
    let numberWithCommas = reversedNumber.replace(/(\d{3})/g, '$1.').split('').reverse().join('');
    if (numberWithCommas.charAt(0) === '.') {
        numberWithCommas = numberWithCommas.substring(1);
    }
    return numberWithCommas;
}

document.addEventListener("DOMContentLoaded", function () {
    let numberElements = document.querySelectorAll("span[id^='number']");

    numberElements.forEach(function (element) {
        let number = parseInt(element.textContent);
        element.textContent = addCommasToNumber(number);
    });
});

function showDropdown() {
    $('.navbar .dropdown').hover(
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }
    );
    $('.dropdown > a').click(function () {
        location.href = this.href;
    });
}