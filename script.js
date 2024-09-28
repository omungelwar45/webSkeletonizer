document.getElementById('skeletonizerForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const url = document.getElementById('urlInput').value;
    fetch(`/skeletonize?url=${encodeURIComponent(url)}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('result').innerHTML = data;
        })
        .catch(error => {
            document.getElementById('result').innerHTML = 'Error fetching the URL: ' + error.message;
        });
});
