document.addEventListener("DOMContentLoaded", function() {
    const flexContainer = document.querySelector('.flex-container');
    const flexItems = document.querySelectorAll('.flex-item');
    const totalItems = flexItems.length;
    const itemsVisible = 3;
    let index = 0;

    // Duplica i primi itemsVisible elementi alla fine del container
    for (let i = 0; i < itemsVisible; i++) {
        const clone = flexItems[i].cloneNode(true);
        flexContainer.appendChild(clone);
    }

    setInterval(() => {
        index++;
        if (index > totalItems) {
            index = 0;
            flexContainer.style.transition = 'none'; // Disabilita la transizione
            flexContainer.style.transform = `translateX(0)`;
            setTimeout(() => {
                flexContainer.style.transition = 'transform 0.5s ease-in-out'; // Ripristina la transizione
            }, 50); // Aspetta un breve momento prima di riabilitare la transizione
        } else {
            flexContainer.style.transform = `translateX(-${index * (100 / itemsVisible)}%)`;
        }
    }, 3000); // Cambia immagine ogni 3 secondi
});