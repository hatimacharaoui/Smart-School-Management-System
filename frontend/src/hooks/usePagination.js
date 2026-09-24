import React, {useEffect, useState} from 'react';

export default function UsePagination(pageBackend, tailleInitiale = 10, cleReinitialisation = "") {

    const [numeroPage, setNumeroPage] = useState(0);
    const [taillePage, setTaillePage] = useState(tailleInitiale);

    useEffect(() => {
        setNumeroPage(0);
    }, [cleReinitialisation]);

    useEffect(() => {
        if (pageBackend.totalPages > 0 && numeroPage >= pageBackend.totalPages) {
            setNumeroPage(pageBackend.totalPages - 1);
        }
    }, [numeroPage, pageBackend.totalPages]);

    function changerPage(nouvellePage) {
        if (
            nouvellePage >= 1 &&
            nouvellePage <= Math.max(1, pageBackend.totalPages)
        ) {
            setNumeroPage(nouvellePage - 1);
        }
    }

    function changerTaillePage(nouvelleTaille) {
        setTaillePage(Number(nouvelleTaille));
        setNumeroPage(0);
    }

    return {
        elementsPage: pageBackend.content,
        pageActuelle: numeroPage + 1,
        nombrePages: Math.max(1, pageBackend.totalPages),
        taillePage,
        totalElements: pageBackend.totalElements,
        numeroPage,
        parametres: { page: numeroPage, size: taillePage },
        changerPage,
        changerTaillePage,
    };
}



export function PageVide(s = 10) {
    return {
        content: [],
        number: 0,
        size: s,
        totalElements: 0,
        totalPages: 0,
    };
}